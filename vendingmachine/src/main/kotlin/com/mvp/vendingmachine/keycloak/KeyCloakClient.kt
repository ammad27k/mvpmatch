package com.mvp.vendingmachine.keycloak

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.keycloak.config.KeyCloakProperties
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.ws.rs.ClientErrorException
import javax.ws.rs.core.Response


@Service
class KeyCloakClient(private val adminKc: Keycloak,
                    private val properties: KeyCloakProperties) {
    val log: Logger = LoggerFactory.getLogger(this::class.java)


    fun signupUser(userId: String?,
                   email: String?,
                   plainPassword: String?,
                   role : String?): String? {
        val realmResource: RealmResource = adminKc.realm(properties.realm)

        val users = realmResource.users()

        val user: UserRepresentation = prepareUser(email)
        val password: CredentialRepresentation = preparePlainPassword(plainPassword)

        val credentials: MutableList<CredentialRepresentation> = ArrayList()
        credentials.add(password)
        user.credentials = credentials

        user.attributes = setupAttributes(userId)
        user.clientRoles = setupRoles(role)

        return try {
            val response: Response = users.create(user)
            if (response.status === 409) {
                throw ApplicationException(ErrorCodes.USER_ALREADY_EXISTS, "User Already exists" )
            }
            val ssoUserId: String = extractSsoUserId(response)
            assignRoleToUser(ssoUserId, role, realmResource)
            ssoUserId
        }  catch (e: ClientErrorException) {
            log.error("ClientErrorException", e)
            //TODO review and remove only for debug purpose
            throw e
        } catch (e: Exception) {
            log.error("Exception", e)
            val cause = e.cause
            if (cause is ClientErrorException) {
                //TODO review and remove
            } else {
                //TODO review and remove
            }
            throw e
        }
    }

    fun assignRoleToUser(userId: String?,
                         role : String?,
                        realmResource: RealmResource) {

        val appClient: ClientRepresentation = realmResource.clients()
            .findByClientId("microservice")[0] // TODO get from configuration file
        val userClientRole = realmResource.clients()[appClient.id]
            .roles()[role].toRepresentation()

        val userResource = realmResource.users().get(userId)
        userResource.roles()
            .clientLevel(appClient.id).add(listOf(userClientRole))
        log.debug("Role id $role is assign to user $userId ")
    }

    private fun extractSsoUserId(response: Response): String {
        val location = response.metadata["Location"]!![0] as String
        val uuid = location.substring(location.lastIndexOf("/") + 1)

        //verifying UUID is properly formed
        return UUID.fromString(uuid).toString()
    }

    private fun prepareUser(email: String?): UserRepresentation {
        val ur = UserRepresentation()
        ur.email = email
        ur.username = email
        ur.isEmailVerified = true
        ur.isEnabled = true
        return ur
    }

    private fun preparePlainPassword(password: String?): CredentialRepresentation {
        val cr = CredentialRepresentation()
        cr.type = CredentialRepresentation.PASSWORD
        cr.isTemporary = false
        cr.value = password
        return cr
    }

    private fun setupAttributes(userId: String?): Map<String, List<String?>> {
        val attributes: MutableMap<String, List<String?>> = HashMap()
        attributes["userId"] = listOf(userId)
        return attributes
    }

    private fun setupRoles(role: String?): Map<String, List<String?>> {
        val attributes: MutableMap<String, List<String?>> = HashMap()
        attributes["clientRoles"] = listOf(role)
        return attributes
    }
}