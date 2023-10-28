package gre.application.services

import org.springframework.stereotype.Service
import java.io.Serializable

@Service
open class GreetService : Serializable {
    fun greet(name: String?): String = when {
        !name.isNullOrEmpty() -> "Hello $name"
        else -> "Hello user"
    }
}
