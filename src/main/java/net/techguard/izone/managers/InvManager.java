package net.techguard.izone.Managers;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvManager {
	public static void addToInventory(Inventory inv, ItemStack itemstack) {
		Material item   = itemstack.getType();
		short    data   = itemstack.getData().getData();
		int      amount = itemstack.getAmount();

		for (int slot = 0; slot < inv.getContents().length; slot++)
		{
			ItemStack itemstackS = inv.getItem(slot);
			if (itemstackS == null)
			{
				itemstackS = new ItemStack(Material.AIR, 0, (short) 0);
			}
			Material itemS   = itemstackS.getType();
			short    dataS   = itemstackS.getData().getData();
			int      amountS = itemstackS.getAmount();

			if (amount == 0)
			{
				break;
			}
			if ((itemS == Material.AIR) || ((item == itemS) && (data == dataS)))
			{
				if (amount + amountS > item.getMaxStackSize())
				{
					int off = amount + amountS;
					int max = item.getMaxStackSize();

					if (item == itemS)
					{
						itemstackS.setAmount(off - (off - max));
					}
					else
					{
						itemstackS.setType(item);
						itemstackS.setDurability(data);
						itemstackS.setAmount(off - (off - max));
					}
					amount = off - max;
				}
				else
				{
					if (item == itemS)
					{
						itemstackS.setAmount(amount + amountS);
					}
					else
					{
						itemstackS.setType(item);
						itemstackS.setDurability(data);
						itemstackS.setAmount(amount + amountS);
					}
					amount = 0;
				}
				inv.setItem(slot, itemstackS);
			}
		}
	}

	public static void removeFromInventory(Inventory inv, ItemStack itemstack) {
		Material item   = itemstack.getType();
		short    data   = itemstack.getData().getData();
		int      amount = itemstack.getAmount();

		boolean flag = amount == -1;
		for (int slot = 0; slot < inv.getContents().length; slot++)
		{
			ItemStack itemstackS = inv.getItem(slot);
			if (itemstackS == null)
			{
				itemstackS = new ItemStack(Material.AIR, 0, (short) 0);
			}
			Material itemS   = itemstackS.getType();
			short    dataS   = itemstackS.getData().getData();
			int      amountS = itemstackS.getAmount();

			if ((!flag) && (amount == 0))
			{
				break;
			}
			if (((item == Material.AIR) || (item == itemS)) && ((data == dataS) || (data == -1)))
			{
				if (flag)
				{
					inv.setItem(slot, null);
				}
				else
				{
					if (amountS - amount <= 0)
					{
						int off = Math.abs(amountS - amount);

						itemstackS = null;
						amount = off;
					}
					else
					{
						itemstackS.setAmount(amountS - amount);
						amount = 0;
					}
					inv.setItem(slot, itemstackS);
				}
			}
		}
	}
}