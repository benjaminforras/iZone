package net.techguard.izone.Commands.zmod;

import net.techguard.izone.Managers.ZoneManager;
import net.techguard.izone.Utils.MessagesAPI;
import net.techguard.izone.Variables;
import net.techguard.izone.Zones.Flags;
import net.techguard.izone.Zones.Zone;
import net.techguard.izone.iZone;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;

import static net.techguard.izone.Utils.Localization.I18n.tl;

public class flagCommand extends zmodBase {
	public flagCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		String name = cmd[2];
		String flag = cmd[3];

		if (ZoneManager.getZone(name) != null) {
			Zone zone = ZoneManager.getZone(name);
			if ((!zone.getOwners().contains(player.getName())) && (!player.hasPermission(Variables.PERMISSION_OWNER))) {
				player.sendMessage(iZone.getPrefix() + tl("zone_notowner"));
				return;
			}
			String text = "";
			if (cmd.length >= 5) {
				for (int i = 4; i < cmd.length; i++) {
					text = text + cmd[i] + " ";
				}
				if (text.endsWith(" ")) {
					text = text.substring(0, text.length() - 1);
				}
			}

			for (Flags flag2 : Flags.values()) {
				if (flag2.toString().toLowerCase().startsWith(flag.toLowerCase())) {
					if (!player.hasPermission(Variables.PERMISSION_FLAGS + flag2.toString())) {
						player.sendMessage(iZone.getPrefix() + tl("zone_flag_no_permission"));
						return;
					}
					if ((flag2 == Flags.WELCOME) || (flag2 == Flags.FAREWELL)) {
						if (text.length() > 0) {
							if (flag2 == Flags.WELCOME) {
								zone.setWelcome(text);
							}
							if (flag2 == Flags.FAREWELL) {
								zone.setFarewell(text);
							}
							player.sendMessage(iZone.getPrefix() + tl("zone_flag_data", flag2.getName()));
							if (zone.hasFlag(flag2)) {
								return;
							}
						} else if (!zone.hasFlag(flag2)) {
							player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_hint"));
						}
					}
					short data;
					int   amount;
					if ((flag2 == Flags.GIVEITEM_IN) || (flag2 == Flags.GIVEITEM_OUT) || (flag2 == Flags.TAKEITEM_IN) || (flag2 == Flags.TAKEITEM_OUT)) {
						if (text.length() > 0) {
							if (text.startsWith("+ ")) {
								ItemStack item = getItemStack(text = text.replaceFirst("\\+ ", ""));

								if (item != null) {
									zone.addInventory(flag2, item);
									Material type = item.getType();
									data = item.getData().getData();
									amount = item.getAmount();
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_add", zone.getName(), flag2.getName(), (type == Material.AIR ? "All" : type.name()), (data == -1 ? "All" : Short.valueOf(data)), (amount == -1 ? "All" : Integer.valueOf(amount))));
								} else {
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error", text));
								}
							} else if (text.startsWith("- ")) {
								ItemStack item = getItemStack(text = text.replaceFirst("\\- ", ""));

								if (item != null) {
									zone.removeInventory(flag2, item);
									Material type = item.getType();
									data = item.getData().getData();
									amount = item.getAmount();
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_remove", zone.getName(), flag2.getName(), (type == Material.AIR ? "All" : type.name()), (data == -1 ? "All" : Short.valueOf(data)), (amount == -1 ? "All" : Integer.valueOf(amount))));
								} else {
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error", text));
								}
							} else {
								player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error2"));
							}
							return;
						}
						if (!zone.hasFlag(flag2)) {
							player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_hint2"));
						}
					} else if ((flag2 == Flags.GIVEEFFECT_IN) || (flag2 == Flags.GIVEEFFECT_OUT) || (flag2 == Flags.TAKEEFFECT_IN) || (flag2 == Flags.TAKEEFFECT_OUT)) {
						if (text.length() > 0) {
							if (text.startsWith("+ ")) {
								PotionEffect potionEffect = getPotionEffect(text = text.replaceFirst("\\+ ", ""));

								if(potionEffect == null)
								{
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error4"));
									player.sendMessage(Arrays.toString(PotionEffectType.values()));
									return;
								}

								if (potionEffect != null) {
									zone.addEffect(flag2, potionEffect);

									/*Potion potion = new Potion(PotionType.getByEffect(potionEffect.getType()));
									potion.getEffects().clear();
									potion.getEffects().addAll(Collections.singleton(potionEffect));*/

									ItemStack potionItem = new ItemStack(Material.POTION);
									ItemMeta  meta       = potionItem.getItemMeta();
									if (meta == null) {
										meta = Bukkit.getItemFactory().getItemMeta(Material.POTION);
									}
									PotionMeta potionMeta = (PotionMeta) meta;
									potionMeta.addCustomEffect(potionEffect, true);
									potionItem.setItemMeta(potionMeta);

									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_add2"));
									MessagesAPI.sendItemTooltipMessage(player, "- " + potionEffect.getType().getName(), potionItem);
									//player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_add", zone.getName(), flag2.getName(), type.getName(), amplifier + ":" + duration));
								} else {
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error", text));
								}
							} else if (text.startsWith("- ")) {
								PotionEffect potionEffect = getPotionEffect(text = text.replaceFirst("\\- ", ""));

								if(potionEffect == null)
								{
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error4"));
									player.sendMessage(Arrays.toString(PotionEffectType.values()));
									return;
								}

								if (potionEffect != null) {
									zone.removeEffect(flag2, potionEffect);

									ItemStack potionItem = new ItemStack(Material.POTION);
									ItemMeta  meta       = potionItem.getItemMeta();
									if (meta == null) {
										meta = Bukkit.getItemFactory().getItemMeta(Material.POTION);
									}
									PotionMeta potionMeta = (PotionMeta) meta;
									potionMeta.addCustomEffect(potionEffect, true);
									potionItem.setItemMeta(potionMeta);

									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_remove2"));
									MessagesAPI.sendItemTooltipMessage(player, "- " + potionEffect.getType().getName(), potionItem);
								} else {
									player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error", text));
								}
							} else {
								player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_error3"));
							}
							return;
						}
						if (!zone.hasFlag(flag2)) {
							player.sendMessage(iZone.getPrefix() + tl("zone_flag_data_hint3"));
						}
					}

					if (flag2 == Flags.GAMEMODE) {
						if (text.length() > 0) {
							GameMode   gm = null;
							GameMode[] arrayOfGameMode;
							amount = (arrayOfGameMode = GameMode.values()).length;
							for (data = 0; data < amount; data++) {
								GameMode mode = arrayOfGameMode[data];
								if (mode.name().toLowerCase().startsWith(text.toLowerCase())) {
									gm = mode;
								}
							}
							if (gm != null) {
								zone.setGamemode(gm);
								player.sendMessage(iZone.getPrefix() + tl("zone_flag_gamemode_change", gm.name().toLowerCase()));
								if (zone.hasFlag(flag2)) {
									return;
								}
							} else {
								player.sendMessage(iZone.getPrefix() + tl("zone_flag_gamemode_error"));
							}
						} else if (!zone.hasFlag(flag2)) {
							player.sendMessage(iZone.getPrefix() + tl("zone_flag_gamemode_hint"));
						}
					}

					if (flag2 == Flags.TELEPORT) {
						Location location = player.getLocation();

						Border border = new Border(zone.getBorder1().toVector(), zone.getBorder2().toVector());
						if (!border.contains(player.getLocation())) {
							player.sendMessage(iZone.getPrefix() + ChatColor.RED + tl("flag_teleport_not_in_zone"));
							return;
						}

						if (isSafeLocation(location)) {
							zone.setTeleport(location);
							player.sendMessage(iZone.getPrefix() + ChatColor.GOLD + tl("flag_teleport_set"));
							if (zone.hasFlag(flag2)) {
								return;
							}
						} else player.sendMessage(iZone.getPrefix() + ChatColor.RED + tl("flag_teleport_notsafe"));
					}

					zone.setFlag(flag2.getId(), !zone.hasFlag(flag2));
					player.sendMessage(iZone.getPrefix() + tl("flag_set", flag2.getName(), (zone.hasFlag(flag2) ? ChatColor.GREEN + "" + ChatColor.BOLD + "ON" : ChatColor.RED + "" + ChatColor.BOLD + "OFF")));
				}
			}
		} else {
			player.sendMessage(iZone.getPrefix() + tl("zone_not_found"));
		}
	}

	public int getLength() {
		return 4;
	}

	public String[] getInfo() {
		return new String[]{"flag", " <zone> <flag> [flag data]", "Toggle a zone flag"};
	}

	public String getError(int i) {
		return getError(null, i);
	}

	@SuppressWarnings({"SameReturnValue", "UnusedParameters"})
	public String getError(Player player, int i) {
		if (player != null) {
			String flags = "";
			for (Flags f : Flags.values()) {
				if (player.hasPermission(Variables.PERMISSION_FLAGS + f.toString())) {
					flags = flags + "§f" + f.toString() + "§c, ";
				}
			}
			if (flags.endsWith("§c, ")) {
				flags = flags.substring(0, flags.length() - 4);
			}
			player.sendMessage("§cAvailable Flags: " + flags);
		}
		return "§cUsage: /zmod flag <zone> <flag>";
	}

	public String getPermission() {
		return Variables.PERMISSION_FLAG;
	}

	private ItemStack getItemStack(String text) {
		String item   = text;
		String data   = "";
		String amount = "";

		if (text.equals("*")) {
			return new ItemStack(Material.AIR, -1, (short) -1);
		}

		if (text.contains(",")) {
			String[] split = item.split(",");
			item = split[0];
			amount = split[1];
		}
		if (item.contains(":")) {
			String[] split = item.split(":");
			item = split[0];
			data = split[1];
		}
		Material item0 = Material.matchMaterial(item);
		if ((item.equals("*")) || (item.equals("-1"))) {
			item0 = Material.AIR;
		}
		if (item0 == null) {
			return null;
		}
		short data0   = 0;
		int   amount0 = 1;
		try {
			if ((data.equals("*")) || (data.equals("-1"))) {
				data0 = -1;
			} else if (!data.equals("")) {
				data0 = (short) Integer.parseInt(data);
			}
		} catch (Exception ignored) {
		}
		try {
			if ((amount.equals("*")) || (amount.equals("-1"))) {
				amount0 = -1;
			} else if (!amount.equals("")) {
				amount0 = Integer.parseInt(amount);
			}
		} catch (Exception ignored) {
		}
		return new ItemStack(item0, amount0, data0);
	}

	// effect,duration,amplifier
	private PotionEffect getPotionEffect(String text) {
		String effectTypeString      = text;
		String effectDurationString  = "";
		String effectAmplifierString = "";

		if (text.equals("*")) {
			return new PotionEffect(PotionEffectType.LUCK, -1, (short) -1);
		}

		if (text.contains(",")) {
			String[] split = effectTypeString.split(",");
			effectTypeString = split[0];
			effectDurationString = split[1];
			if (split.length > 2) {
				effectAmplifierString = split[2];
			}
		}

		PotionEffectType effectType = PotionEffectType.getByName(effectTypeString);
		if(effectType == null)
		{
			return null;
		}

		if ((effectTypeString.equals("*")) || (effectTypeString.equals("-1"))) {
			effectType = PotionEffectType.LUCK;
		}
		if (effectType == null) {
			return null;
		}

		int effectDuration  = 1;
		int effectAmplifier = 1;
		try {
			if ((effectAmplifierString.equals("*")) || (effectAmplifierString.equals("-1"))) {
				effectAmplifier = -1;
			} else if (!effectAmplifierString.equals("")) {
				effectAmplifier = Integer.parseInt(effectAmplifierString);
			}
		} catch (Exception ignored) {
		}

		try {
			if ((effectDurationString.equals("*")) || (effectDurationString.equals("-1"))) {
				effectDuration = -1;
			} else if (!effectDurationString.equals("")) {
				effectDuration = Integer.parseInt(effectDurationString);
			}
		} catch (Exception ignored) {
		}
		return new PotionEffect(effectType, effectDuration*20, effectAmplifier);
	}

	private boolean isSafeLocation(Location location) {

		Block feet = location.getBlock();
		if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
			return false;
		}

		Block head = feet.getRelative(BlockFace.UP);
		if (!head.getType().isTransparent()) {
			return false;
		}

		Block ground = feet.getRelative(BlockFace.DOWN);
		if (!ground.getType().isSolid()) {
			return false;
		}
		return true;
	}

	public class Border {

		private Vector p1;
		private Vector p2;


		public Border(Vector p1, Vector p2) {
			int x1 = Math.min(p1.getBlockX(), p2.getBlockX());
			int y1 = Math.min(p1.getBlockY(), p2.getBlockY());
			int z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
			int x2 = Math.max(p1.getBlockX(), p2.getBlockX());
			int y2 = Math.max(p1.getBlockY(), p2.getBlockY());
			int z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
			this.p1 = new Vector(x1, y1, z1);
			this.p2 = new Vector(x2, y2, z2);
		}

		public boolean contains(Location loc) {
			if (loc == null) {
				return false;
			}
			return loc.getBlockX() >= p1.getBlockX() && loc.getBlockX() <= p2.getBlockX()
					&& loc.getBlockY() >= p1.getBlockY() && loc.getBlockY() <= p2.getBlockY()
					&& loc.getBlockZ() >= p1.getBlockZ() && loc.getBlockZ() <= p2.getBlockZ();
		}

	}
}