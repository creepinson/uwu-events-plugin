package dev.throwouterror.bukkit.uwu.events.data

import com.google.common.collect.ImmutableMap
import dev.throwouterror.bukkit.uwu.events.core.EventPlugin
import dev.throwouterror.bukkit.uwu.events.util.BungeeUtils
import jdk.nashorn.internal.runtime.ECMAException.CREATE
import org.bukkit.entity.Player
import org.hibernate.cfg.AvailableSettings.*
import org.hibernate.dialect.Oracle12cDialect
import org.hibernate.jpa.AvailableSettings.JDBC_DRIVER
import org.hibernate.jpa.AvailableSettings.JDBC_URL
import org.hibernate.jpa.HibernatePersistenceProvider
import javax.persistence.EntityTransaction
import javax.persistence.NoResultException
import javax.persistence.Query
import javax.persistence.TypedQuery


object EventManager {
    /**
     * @param eventName  The name of the event
     * @param serverName The name of the BungeeCord server or world for players to connect to.
     */
    fun create(eventName: String, serverName: String) {
        val em = EventPlugin.instance.entityManagerFactory.createEntityManager();
        var et: EntityTransaction? = null
        try {
            et = em.transaction
            et.begin()
            val event = Event()
            event.name = eventName
            event.server = serverName
            event.enabled = false
            em.persist(event)
            et.commit()
        } catch (e: Exception) {
            et?.rollback()
            e.printStackTrace()
        } finally {
            em.close()
        }
    }

    fun get(id: Int): Event? {
        val em = EventPlugin.instance.entityManagerFactory.createEntityManager()
        val query = "SELECT e FROM Event e WHERE e.id = :id"
        val tq: TypedQuery<Event> = em.createQuery(query, Event::class.java)
        tq.setParameter("id", id)
        var e: Event? = null
        try {
            e = tq.singleResult
        } catch (e: NoResultException) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return e
    }

    fun toggle(id: Int, enabled: Boolean) {
        val em = EventPlugin.instance.entityManagerFactory.createEntityManager()
        em.transaction.begin();

        val query = "UPDATE Event e SET enabled=:enabled WHERE e.id = :id"
        val tq: Query = em.createQuery(query)
        tq.setParameter("id", id)
        tq.setParameter("enabled", enabled)
        try {
            tq.executeUpdate()
            em.transaction.commit()
        } catch (e: NoResultException) {
            em.transaction.rollback()
            e.printStackTrace()
        } finally {
            em.close()
        }
    }


    fun remove(id: Int): Event? {
        val em = EventPlugin.instance.entityManagerFactory.createEntityManager()
        val query = "SELECT e FROM Event e WHERE e.id = :id"
        val tq: TypedQuery<Event> = em.createQuery(query, Event::class.java)
        tq.setParameter("id", id)
        var e: Event? = null
        try {
            e = tq.singleResult
            em.remove(e)
        } catch (e: NoResultException) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return e
    }

    fun list(): List<Event> {
        val em = EventPlugin.instance.entityManagerFactory.createEntityManager()
        val query = "SELECT e FROM Event e WHERE e.id IS NOT NULL"
        val tq: TypedQuery<Event> = em.createQuery(query, Event::class.java)
        var e: List<Event> = arrayListOf()
        try {
            e = tq.resultList
        } catch (e: NoResultException) {
            e.printStackTrace()
        } finally {
            em.close()
        }
        return e
    }

    /**
     * @param players   The players to send to the event
     * @param eventName The name of the event
     */
    fun join(id: Int, players: List<Player>): Boolean {
        val event: Event? = get(id)
        return if (event != null) {
            val serverName = event.server
            val enabled: Boolean = event.enabled
            if (serverName.isNotEmpty()) {
                if (!enabled) return false
                for (p in players)
                    BungeeUtils.sendPlayerToServer(p, serverName)
                true
            } else
                false
        } else
            false
    }
}