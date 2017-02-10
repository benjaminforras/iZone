package net.techguard.izone.Zones;

import net.techguard.izone.iZone;
import net.techguard.izone.Managers.ZoneManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Zone {
	private final HashMap<Flags, ArrayList<ItemStack>> inventory;
	private final boolean[] flags = new boolean[Flags.values().length];
	private final YamlConfiguration saveFile;
	private String            name         = "";
	private World             world        = null;
	private Location          border1      = null;
	private Location          border2      = null;
	private long              creationDate = 0L;
	private String            welcome      = "";
	private String            farewell     = "";
	private GameMode          gamemode     = GameMode.SURVIVAL;
	private boolean           cansave      = true;
	private ArrayList<String> allowed;
	private HashMap<Flags, ArrayList<PotionEffect>> effects;
	private String            parent       = "";
	private Location location;

	public Zone(String s) {
		this.name = s;
		this.saveFile = YamlConfiguration.loadConfiguration(getSaveFile());
		this.allowed = new ArrayList<>();
		this.inventory = new HashMap<>();
		this.effects = new HashMap<>();
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<String> getOwners() {
		ArrayList<String> owners = new ArrayList<String>();
		for (String a : this.allowed)
		{
			if (!a.startsWith("o:"))
			{
				continue;
			}
			owners.add(a.substring(2));
		}
		return owners;
	}

	public Location getBorder1() {
		return this.border1;
	}

	public Location getBorder2() {
		return this.border2;
	}

	public ArrayList<ItemStack> getInventory(Flags a) {
		if (this.inventory.containsKey(a))
		{
			return this.inventory.get(a);
		}
		return new ArrayList<ItemStack>();
	}

	public ArrayList<PotionEffect> getEffects(Flags a) {
		if (this.effects.containsKey(a))
		{
			return this.effects.get(a);
		}
		return new ArrayList<PotionEffect>();
	}

	public ArrayList<String> getAllowed() {
		return this.allowed;
	}

	public void setAllowed(ArrayList<String> a) {
		this.allowed = a;
		setProperty("allowed", a);
	}

	public boolean hasFlag(Flags a) {
		boolean flag = this.flags[a.getId()];
		if ((getParent() != null) && (!flag))
		{
			flag = getParent().hasFlag(a);
		}
		return flag;
	}

	public String getWelcome() {
		if ((getParent() != null) && (this.welcome.equals("")))
		{
			return getParent().getWelcome();
		}

		return this.welcome;
	}

	public void setWelcome(String a) {
		this.welcome = a;
		setProperty("welcome", a);
	}

	public String getFarewell() {
		if ((getParent() != null) && (this.farewell.equals("")))
		{
			return getParent().getFarewell();
		}

		return this.farewell;
	}

	public void setFarewell(String a) {
		this.farewell = a;
		setProperty("farewell", a);
	}

	public GameMode getGamemode() {
		return this.gamemode;
	}

	public void setGamemode(GameMode a) {
		this.gamemode = a;
		setProperty("gamemode", a.name());
	}

	public Location getTeleport()
	{
		return this.location;
	}

	public void setTeleport(Location location)
	{
		this.location = location;
		setProperty("teleport", (location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ()));
	}

	public World getWorld() {
		return this.world;
	}

	public void setWorld(World a) {
		this.world = a;
		setProperty("world", a.getName());
	}

	public ArrayList<Flags> getFlags() {
		ArrayList<Flags> list = new ArrayList<Flags>();
		for (Flags flag : Flags.values())
		{
			if (!hasFlag(flag))
			{
				continue;
			}
			list.add(flag);
		}

		return list;
	}

	public ArrayList<Flags> getAllFlags() {
		ArrayList<Flags> list = new ArrayList<Flags>();
		Collections.addAll(list, Flags.values());

		return list;
	}

	public Zone getParent() {
		Zone parent = null;
		if (this.parent.length() > 0)
		{
			parent = ZoneManager.getZone(this.parent);
			if (parent == null)
			{
				setParent("");
			}
		}
		return parent;
	}

	public void setParent(String a) {
		this.parent = a;
		setProperty("parent-zone", this.parent);
	}

	public boolean canSave() {
		return this.cansave;
	}

	public File getSaveFile() {
		File file = new File(iZone.instance.getDataFolder() + File.separator + "saves" + File.separator + this.name + ".yml");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	public long getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Long a) {
		this.creationDate = a;
		setProperty("creation-date", this.creationDate);
	}

	public void setBorder(int a, Location b) {
		if (a == 1)
		{
			this.border1 = b;
			setProperty("border1.x", b.getBlockX());
			setProperty("border1.y", b.getBlockY());
			setProperty("border1.z", b.getBlockZ());
		}
		else if (a == 2)
		{
			this.border2 = b;
			setProperty("border2.x", b.getBlockX());
			setProperty("border2.y", b.getBlockY());
			setProperty("border2.z", b.getBlockZ());
		}
		setWorld(b.getWorld());
	}

	public void setFlag(Integer a, boolean b) {
		this.flags[a] = b;
		setProperty("flag." + Flags.values()[a].toString(), b);
	}

	public void addInventory(Flags a, ItemStack b) {
		if (!this.inventory.containsKey(a))
		{
			this.inventory.put(a, new ArrayList<>());
		}
		this.inventory.get(a).add(b);
		saveInventory(a);
	}

	public void addEffect(Flags a, PotionEffect b) {
		if (!this.effects.containsKey(a))
		{
			this.effects.put(a, new ArrayList<>());
		}
		this.effects.get(a).add(b);
		saveEffects(a);
	}

	public void removeInventory(Flags a, ItemStack b) {
		if (this.inventory.containsKey(a) && this.inventory.get(a).contains(b))
		{
			this.inventory.get(a).remove(b);
		}

		saveInventory(a);
	}

	public void removeEffect(Flags a, PotionEffect b) {
		if (this.effects.containsKey(a) && this.effects.get(a).contains(b))
		{
			this.effects.get(a).remove(b);
		}

		saveEffects(a);
	}

	public void saveInventory(Flags a) {
		if (this.inventory.get(a) == null)
		{
			return;
		}
		List<String> items = new ArrayList<>();

		for (ItemStack item : this.inventory.get(a))
		{
			if (item != null)
			{
				items.add("(" + item.getTypeId() + ", " + item.getAmount() + ", " + item.getData().getData() + ")");
			}
		}
		setProperty("inventory." + a.toString(), items);
	}

	public void saveEffects(Flags a) {
		if (this.effects.get(a) == null)
		{
			return;
		}
		List<String> items = new ArrayList<>();

		for (PotionEffect effect : this.effects.get(a))
		{
			if (effect != null)
			{
				items.add("(" + effect.getType() + ", " + effect.getDuration() + ", " + effect.getAmplifier() + ")");
			}
		}
		setProperty("effects." + a.toString(), items);
	}

	public boolean Add(String a) {
		if (!this.allowed.contains(a))
		{
			this.allowed.add(a);
			setProperty("allowed", this.allowed);
			return true;
		}
		return false;
	}

	public boolean Remove(String a) {
		if (this.allowed.contains(a))
		{
			this.allowed.remove(a);
			setProperty("allowed", this.allowed);
			return true;
		}
		return false;
	}

	public void resetOwners() {
		for (String s : getOwners())
		{
			s = "o:" + s;
			Remove(s);
		}
	}

	public void setSave(boolean a) {
		this.cansave = a;
	}

	private void setProperty(String key, Object value) {
		if (canSave())
		{
			try
			{
				this.saveFile.load(getSaveFile());
				this.saveFile.set(key, value);
				this.saveFile.save(getSaveFile());
			} catch (Exception e)
			{
				e.printStackTrace();
				iZone.instance.getLogger().info("Couldn't set property '" + key + "' for zone '" + getName() + "'");
			}
		}
	}
}