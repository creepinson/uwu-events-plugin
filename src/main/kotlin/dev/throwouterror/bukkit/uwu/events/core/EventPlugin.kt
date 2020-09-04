package dev.throwouterror.bukkit.uwu.events.core

import dev.throwouterror.bukkit.uwu.events.command.EventCommand
import dev.throwouterror.bukkit.uwu.events.data.persistenceUnitInfo
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.cfg.AvailableSettings
import org.hibernate.jpa.HibernatePersistenceProvider
import javax.persistence.EntityManagerFactory

class EventPlugin : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: EventPlugin
    }

    override fun onEnable() {
        // Fired when the server enables the plugin
        instance = getPlugin(EventPlugin::class.java)
        //        PluginDescriptionFile pdfFile = this.getDescription();
        this.config.options().copyDefaults(true)
        saveDefaultConfig()

        entityManagerFactory = HibernatePersistenceProvider().createContainerEntityManagerFactory(
                persistenceUnitInfo(),
                mutableMapOf(
                        AvailableSettings.DRIVER to config.getString("database.driver"),
                        AvailableSettings.URL to "jdbc:mysql://" + config.getString("database.host") + "/" + config.getString("database.name"),
                        AvailableSettings.USER to config.getString("database.user"),
                        AvailableSettings.PASS to config.getString("database.password"),
                        AvailableSettings.DIALECT to config.getString("database.dialect"),
                        AvailableSettings.HBM2DDL_AUTO to "create-only"))

        getCommand("event")?.setExecutor(EventCommand())
    }

    override fun onDisable() {
        // Fired when the server stops and disables all plugins
        entityManagerFactory.close()
    }

    lateinit var entityManagerFactory: EntityManagerFactory
}