package Bot;

import ClientContext.Teleporter.Teleports;
import paint.PaintProvider;
import simple.api.script.Category;
import simple.api.script.LoopingScript;
import simple.api.script.ScriptManifest;

@ScriptManifest(author = "Vainiven", category = Category.CRAFTING, description = "This will tan all of your d'hides. Specially made for iron mans.", name = "V-Tanner", version = "2.0", discord = "Vainiven#6986", servers = {
		"Xeros" })

public class Hider extends VScript implements LoopingScript {
	int cmd3;
	private final String[] hides = { "Cowhide", "Snake hide", "Green dragonhide", "Blue dragonhide", "Red dragonhide",
			"Black dragonhide" };

	private static int tannedhides = 0, trips = 0;

	public Hider() {
		super("https://i.imgur.com/OH51nfO.png", new PaintProvider("Hides Tanned", () -> tannedhides),
				new PaintProvider("Trips", () -> trips));
	}

	@Override
	public void onProcess() {
		if (!players.inRegion(15159)) {
			teleports.teleport(Teleports.SKILLINGISLAND);
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
				final String item = ctx.inventory.populate().filter(hides).next().getName();
				if ("Cowhide".equals(item)) {
					cmd3 = 14793;
				}
				if ("Snake hide".equals(item)) {
					cmd3 = 14795;
				}
				if ("Green dragonhide".equals(item)) {
					cmd3 = 14797;
				}
				if ("Blue dragonhide".equals(item)) {
					cmd3 = 14798;
				}
				if ("Red dragonhide".equals(item)) {
					cmd3 = 14799;
				}
				if ("Black dragonhide".equals(item)) {
					cmd3 = 14800;
				}
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
