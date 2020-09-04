package dev.throwouterror.bukkit.uwu.events.data

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "Event")
class Event : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    var id: Int = 0;

    @Column(nullable = false)
    var name: String = "New Event"

    @Column(nullable = false)
    var server: String = "event"

    @Column(nullable = false)
    var enabled: Boolean = false
}