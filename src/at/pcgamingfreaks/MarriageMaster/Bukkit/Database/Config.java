/*
 *   Copyright (C) 2019 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.pcgamingfreaks.MarriageMaster.Bukkit.Database;

import at.pcgamingfreaks.Bukkit.Configuration;
import at.pcgamingfreaks.MarriageMaster.Bukkit.Database.Helper.OldFileUpdater;
import at.pcgamingfreaks.MarriageMaster.Bukkit.MarriageMaster;
import at.pcgamingfreaks.MarriageMaster.Bukkit.SpecialInfoWorker.UpgradedInfo;
import at.pcgamingfreaks.MarriageMaster.Database.DatabaseConfiguration;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.*;

public class Config extends Configuration implements DatabaseConfiguration
{
	private static final int CONFIG_VERSION = 93, UPGRADE_THRESHOLD = 93, PRE_V2_VERSIONS = 90;
	
	public Config(JavaPlugin plugin)
	{
		super(plugin, CONFIG_VERSION, UPGRADE_THRESHOLD);
		languageKey = "Language.Language";
		languageUpdateKey = "Language.UpdateMode";
	}

	@Override
	protected void doUpgrade(at.pcgamingfreaks.YamlFileManager oldConfig)
	{
		if(oldConfig.getVersion() < PRE_V2_VERSIONS)
		{
			OldFileUpdater.updateConfig(oldConfig.getYaml(), this.getConfig());
			new UpgradedInfo(MarriageMaster.getInstance());
		}
		else
		{
			super.doUpgrade(oldConfig);
		}
	}

	@Override
	protected void doUpdate()
	{
		// We don't have to update our config by now :)
	}

	//region Getter
	//region Global settings
	public boolean isPolygamyAllowed()
	{
		return getConfig().getBoolean("Marriage.AllowPolygamy", false);
	}

	public boolean isSelfMarriageAllowed()
	{
		return !getConfig().getBoolean("Marriage.RequirePriest", true);
	}

	public boolean isSelfDivorceAllowed()
	{
		String val = getConfig().getString("Marriage.DivorceRequiresPriest", "auto").trim().toLowerCase();
		switch(val)
		{
			case "on":
			case "yes":
			case "true": return false;
			case "off":
			case "no":
			case "false": return true;
			default: return isSelfMarriageAllowed();
		}
	}

	public boolean isSurnamesEnabled()
	{
		return getConfig().getBoolean("Marriage.Surnames.Enable", false);
	}

	public boolean isSurnamesForced()
	{
		return getConfig().getBoolean("Marriage.Surnames.Forced", false);
	}

	public double getRange(String option)
	{
		return getConfig().getDouble("Range." + option, 25.0);
	}

	public double getRangeSquared(String option)
	{
		double range = getConfig().getDouble("Range." + option, 625.0);
		return range * range;
	}

	public boolean isMarryAnnouncementEnabled()
	{
		return getConfig().getBoolean("Marriage.AnnounceOnMarriage", true);
	}

	public boolean isSetPriestCommandEnabled()
	{
		return !getConfig().getBoolean("Marriage.DisableSetPriestCommand", false);
	}
	//endregion

	//region Confirmation settings
	public boolean isMarryConfirmationEnabled()
	{
		return getConfig().getBoolean("Marriage.Confirmation.Enable", true);
	}

	public boolean isMarryConfirmationAutoDialogEnabled()
	{
		return getConfig().getBoolean("Marriage.Confirmation.AutoDialog", true);
	}

	public boolean isConfirmationBothPlayersOnDivorceEnabled()
	{
		return getConfig().getBoolean("Marriage.Confirmation.BothPlayersOnDivorce", true);
	}

	public boolean isConfirmationOtherPlayerOnSelfDivorceEnabled()
	{
		return getConfig().getBoolean("Marriage.Confirmation.OtherPlayerOnSelfDivorce", false);
	}
	//endregion

	//region Surname settings
	public boolean getSurnamesAllowColors()
	{
		return getConfig().getBoolean("Marriage.Surnames.AllowColors", false);
	}

	public String getSurnamesAllowedCharacters()
	{
		return getConfig().getString("Marriage.Surnames.AllowedCharacters", "A-Za-z");
	}

	public int getSurnamesMinLength()
	{
		return Math.max(getConfig().getInt("Marriage.Surnames.MinLength", 3), 3);
	}

	public int getSurnamesMaxLength()
	{
		return Math.max(getConfig().getInt("Marriage.Surnames.MaxLength", 16), getSurnamesMinLength());
	}
	//endregion

	//region List command settings
	public boolean useListFooter()
	{
		return getConfig().getBoolean("List.UseFooter", true);
	}

	public int getListEntriesPerPage()
	{
		return getConfig().getInt("List.EntriesPerPage", 8);
	}
	//endregion

	//region Home/TP command settings
	public int getTPDelayTime()
	{
		return (getConfig().getBoolean("Teleport.Delay", false) ? getConfig().getInt("Teleport.DelayTime", 3) : 0);
	}

	public Set<String> getTPBlackListedWorlds()
	{
		Set<String> blackListedWorlds = new HashSet<>();
		for(String world : getConfig().getStringList("Teleport.BlacklistedWorlds", new LinkedList<>()))
		{
			blackListedWorlds.add(world.toLowerCase());
		}
		return blackListedWorlds;
	}

	public boolean getSafetyCheck()
	{
		return getConfig().getBoolean("Teleport.CheckSafety", true);
	}

	public boolean getRequireConfirmation()
	{
		return getConfig().getBoolean("Teleport.RequireConfirmation", true);
	}
	//endregion

	//region Kiss command settings
	public boolean isKissEnabled()
	{
		return getConfig().getBoolean("Kiss.Enable", true);
	}

	public int getKissWaitTime()
	{
		return getConfig().getInt("Kiss.WaitTime", 10) * 1000;
	}

	public int getKissHearthCount()
	{
		return getConfig().getInt("Kiss.HearthCount", 50);
	}
	//endregion

	//region Gift command settings
	public boolean isGiftEnabled()
	{
		return getConfig().getBoolean("Gift.Enable", true);
	}

	public boolean isGiftAllowedInCreative()
	{
		return getConfig().getBoolean("Gift.AllowInCreative", false);
	}
	//endregion

	// PvP command settings
	public boolean getPvPAllowBlocking()
	{
		return getConfig().getBoolean("PvP.AllowBlocking", true);
	}

	//region BonusXP
	public boolean isBonusXPEnabled()
	{
		return getConfig().getBoolean("BonusXp.Enable", false);
	}

	public double getBonusXpMultiplier()
	{
		return getConfig().getDouble("BonusXp.Multiplier", 2);
	}

	public boolean isBonusXPSplitOnPickupEnabled()
	{
		return getConfig().getBoolean("BonusXp.SplitXpOnPickup", true);
	}
	//endregion

	//region HP Regain
	public boolean isHPRegainEnabled()
	{
		return getConfig().getBoolean("HealthRegain.Enable", false);
	}

	public double getHPRegainMultiplier()
	{
		return getConfig().getDouble("HealthRegain.Multiplier", 2);
	}
	//endregion

	//region Database getter
	@Override
	public @NotNull String getDatabaseType()
	{
		return getConfig().getString("Database.Type", "SQLite").toLowerCase();
	}

	public void setDatabaseType(String newType)
	{
		getConfig().set("Database.Type", newType);
		try
		{
			save();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean useUUIDs()
	{
		return getConfig().getBoolean("Database.UseUUIDs", true);
	}

	@Override
	public boolean useUUIDSeparators()
	{
		return getConfig().getBoolean("Database.UseUUIDSeparators", false);
	}

	@Override
	public boolean getUseOnlineUUIDs()
	{
		String type = getConfig().getString("Database.UUID_Type", "auto").toLowerCase();
		if(type.equals("auto"))
		{
			return plugin.getServer().getOnlineMode();
		}
		return type.equals("online");
	}

	@Override
	public @NotNull String getSQLHost()
	{
		return getConfig().getString("Database.SQL.Host", "localhost:3306");
	}

	@Override
	public @NotNull String getSQLDatabase()
	{
		return getConfig().getString("Database.SQL.Database", "minecraft");
	}

	@Override
	public @NotNull String getSQLUser()
	{
		return getConfig().getString("Database.SQL.User", "minecraft");
	}

	@Override
	public @NotNull String getSQLPassword()
	{
		return getConfig().getString("Database.SQL.Password", "minecraft");
	}

	@Override
	public int getSQLMaxConnections()
	{
		return Math.max(1, getConfig().getInt("Database.SQL.MaxConnections", 2));
	}

	@Override
	public @NotNull String getSQLConnectionProperties()
	{
		List<String> list = getConfig().getStringList("Database.SQL.Properties", new LinkedList<>());
		StringBuilder str = new StringBuilder();
		if(list != null)
		{
			for(String s : list)
			{
				str.append("&").append(s);
			}
		}
		return str.toString();
	}

	@Override
	public @NotNull String getSQLTableUser()
	{
		return getConfig().getString("Database.SQL.Tables.User", "marry_players");
	}

	@Override
	public @NotNull String getSQLTableHomes()
	{
		return getConfig().getString("Database.SQL.Tables.Home", "marry_home");
	}

	@Override
	public @NotNull String getSQLTablePriests()
	{
		return getConfig().getString("Database.SQL.Tables.Priests", "marry_priests");
	}

	@Override
	public @NotNull String getSQLTableMarriages()
	{
		return getConfig().getString("Database.SQL.Tables.Partner", "marry_partners");
	}

	@Override
	@Contract("_, !null -> !null")
	public @NotNull String getSQLField(@NotNull String field, @Nullable String defaultValue)
	{
		return getConfig().getString("Database.SQL.Tables.Fields." + field, defaultValue);
	}

	@Override
	public @NotNull String getUnCacheStrategie()
	{
		return getConfig().getString("Database.Cache.UnCache.Strategie", "interval").toLowerCase();
	}

	@Override
	public long getUnCacheInterval()
	{
		return getConfig().getLong("Database.Cache.UnCache.Interval", 600) * 20L;
	}

	@Override
	public long getUnCacheDelay()
	{
		return getConfig().getLong("Database.Cache.UnCache.Delay", 600) * 20L;
	}
	//endregion

	//region Join Leave Info
	public boolean isJoinLeaveInfoEnabled()
	{
		return getConfig().getBoolean("InfoOnPartnerJoinLeave.Enable", true);
	}

	public long getJoinInfoDelay()
	{
		return getConfig().getLong("InfoOnPartnerJoinLeave.JoinDelay", 0) * 20L;
	}
	//endregion

	//region Chat settings
	public boolean isChatEnabled()
	{
		return getConfig().getBoolean("Chat.Enabled", true);
	}

	public boolean isChatSurveillanceEnabled()
	{
		return getConfig().getBoolean("Chat.AllowSurveillance", false);
	}
	//endregion

	//region Prefix/Suffix
	public boolean isPrefixEnabled()
	{
		return getConfig().getBoolean("Prefix.Enable", true);
	}

	public boolean isSuffixEnabled()
	{
		return getConfig().getBoolean("Suffix.Enable", true);
	}

	public String getPrefix()
	{
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix.String", "<heart>{PartnerName}<heart>").replace("<heart>", ChatColor.RED + "\u2764" + ChatColor.WHITE));
	}

	public boolean isPrefixOnLineBeginning()
	{
		return getConfig().getBoolean("Prefix.OnLineBeginning", true);
	}

	public String getSuffix()
	{
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Suffix.String", "<heart>{PartnerName}<heart>").replace("<heart>", ChatColor.RED + "\u2764" + ChatColor.WHITE));
	}
	//endregion

	//region Backpack share
	public boolean isBackpackShareEnabled()
	{
		return getConfig().getBoolean("BackpackShare.Enable", true);
	}
	//endregion

	//region Economy
	public boolean isEconomyEnabled()
	{
		return getConfig().getBoolean("Economy.Enable", false);
	}

	public double getEconomyValue(String valueName)
	{
		return getConfig().getDouble("Economy." + valueName, 0);
	}
	//endregion

	//region Command Executor
	public boolean isCommandExecutorEnabled()
	{
		return getConfig().getBoolean("CommandExecutor.Enable", false);
	}

	public Collection<String> getCommandExecutorOnMarry()
	{
		return getConfig().getStringList("CommandExecutor.OnMarry", new LinkedList<>());
	}

	public Collection<String> getCommandExecutorOnMarryWithPriest()
	{
		return getConfig().getStringList("CommandExecutor.OnMarry", new LinkedList<>());
	}

	public Collection<String> getCommandExecutorOnDivorce()
	{
		return getConfig().getStringList("CommandExecutor.OnDivorce", new LinkedList<>());
	}

	public Collection<String> getCommandExecutorOnDivorceWithPriest()
	{
		return getConfig().getStringList("CommandExecutor.OnDivorceWithPriest", new LinkedList<>());
	}
	//endregion

	//region Misc getter
	public boolean useUpdater()
	{
		return getConfig().getBoolean("Misc.AutoUpdate", true);
	}

	public boolean useUpdaterDevBuilds()
	{
		//TODO add config value for final version
		return true;
	}

	public boolean isBungeeEnabled()
	{
		return getConfig().getBoolean("Misc.UseBungeeCord", false);
	}
	//endregion
	//endregion
}