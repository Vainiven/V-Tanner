package main;

import Bot.VScript;
import paint.PaintProvider;
import simple.api.script.Category;
import simple.api.script.LoopingScript;
import simple.api.script.ScriptManifest;

@ScriptManifest(author = "Vainiven", category = Category.CRAFTING, description = "This will tan all of your d'hides. Specially made for iron mans.", name = "V-Tanner", version = "2.0", discord = "Vainiven#6986", servers = {
		"Xeros" })

public class Hider extends VScript implements LoopingScript {
	int cmd3;
	private String[] hides = { "Cowhide", "Snake hide", "Green dragonhide", "Blue dragonhide", "Red dragonhide",
			"Black dragonhide" };

	int tannedhides;
	int trips;
	
	public Hider() {
		super("https://i.imgur.com/OH51nfO.png");
	}

	@Override
	protected PaintProvider[] getPaintProviders() {
		return new PaintProvider[] { new PaintProvider("Hides Tanned", () -> tannedhides), new PaintProvider("Trips", () -> trips) };
	}

	@Override
	public void onProcess() {
		if (!players.inRegion(15159)) {
			teleports.teleportSkillingIsland();
		} else if (ctx.inventory.populate().filter(hides).isEmpty() || ctx.bank.bankOpen()) {
			if (!ctx.bank.bankOpen()) {
				bank.openBank();
				ctx.bank.depositAllExcept("Coins");
			} else if (!ctx.bank.populate().filter(hides).isEmpty()
					&& ctx.inventory.populate().filter(hides).isEmpty()) {
				ctx.bank.withdraw(ctx.bank.populate().filterContains(hides).next().getId(), 27);
			} else if (ctx.inventory.populate().filter("Coins").population(true) < 50000) {
				ctx.bank.withdraw("Coins", 1000000);
			} else if (ctx.bank.populate().filter(hides).isEmpty()) {
				System.out.println("Out of hides, stopping script.");
				ctx.stopScript();
			} else {
				ctx.bank.closeBank();
			}
		} else if (!ctx.npcs.populate().filter(5809).isEmpty()) {
			if (ctx.widgets.getOpenInterfaceId() != 14670) {
				ctx.npcs.nextNearest().interact(20);
				ctx.onCondition(() -> !ctx.widgets.populate().filter(14670).isEmpty());
			} else {
				String item = ctx.inventory.populate().filter(hides).next().getName();
				if (item.equals("Cowhide"))
					cmd3 = 14793;
				if (item.equals("Snake hide"))
					cmd3 = 14795;
				if (item.equals("Green dragonhide"))
					cmd3 = 14797;
				if (item.equals("Blue dragonhide"))
					cmd3 = 14798;
				if (item.equals("Red dragonhide"))
					cmd3 = 14799;
				if (item.equals("Black dragonhide"))
					cmd3 = 14800;
				ctx.menuActions.sendAction(315, 169, 0, cmd3);
				ctx.onCondition(() -> ctx.inventory.populate().filter(hides).isEmpty());
				trips++;
				tannedhides += 27;
				ctx.shop.closeShop();
			}
		}

	}

	@Override
	public int loopDuration() {
		return 250;
	}

}
