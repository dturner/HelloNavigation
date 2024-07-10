package com.example.hellonavigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
object Contacts

@Serializable
internal data class ContactDetails(val id: String)

fun NavController.navigateToContactDetails(id: String){
    navigate(route = ContactDetails(id = id))
}

fun NavGraphBuilder.contactsDestination(
    onNavigateToContactDetails: (contactId: String) -> Unit
) {
    composable<Contacts> {
        ContactsScreen(onNavigateToContactDetails)
    }
}

fun NavGraphBuilder.contactDetailsDestination(){
    composable<ContactDetails> {
        ContactDetailsScreen(contact = it.toRoute())
    }
}
@Composable
internal fun ContactsScreen(onNavigateToContactDetails: (contactId: String) -> Unit){
    Column {
        Text(text = "I am a contacts screen!")
        Button(onClick = { onNavigateToContactDetails("123") }){
            Text("Billy Bob")
        }
    }
}


@Composable
internal fun ContactDetailsScreen(contact: ContactDetails){
    Text("Contact ID: ${contact.id}")
}