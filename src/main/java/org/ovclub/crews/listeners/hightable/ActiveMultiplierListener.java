package org.ovclub.crews.listeners.hightable;

import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.hightable.VoteItem;
import org.ovclub.crews.utilities.HightableUtility;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.*;

public class ActiveMultiplierListener implements Listener {

    private final Crews plugin;
    private final Map<Player, EnchantmentOffer[]> playerOffers = new HashMap<>();
    private final ArrayList<Integer> levels = new ArrayList<>(Arrays.asList(9, 69, 149, 249));
    private final Map<Merchant, List<MerchantRecipe>> originalRecipes = new HashMap<>();

    public ActiveMultiplierListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockMined(BlockBreakEvent e) {
        PlayerData data = plugin.getData();
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Material m = b.getType();

        if (b.hasMetadata("playerPlaced")) {
            return;
        }

        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem voteItem : topVotes) {
            if (voteItem.getSection().equals("oreDrops")) {
                if (HightableUtility.isValidMaterial(voteItem.getItem())) {
                    if (Material.valueOf(voteItem.getItem()).equals(m)) {
                        Collection<ItemStack> drops = b.getDrops(p.getInventory().getItemInMainHand());
                        for (ItemStack drop : drops) {
                            setItemBonus(voteItem, drop, p);
                        }
                        e.setDropItems(false);
                        World world = b.getWorld();
                        Location location = b.getLocation();
                        for (ItemStack drop : drops) {
                            world.dropItemNaturally(location, drop);
                        }
                    }
                } else if (voteItem.getSection().equals("xpDrops")) {
                    if (HightableUtility.isValidMaterial(voteItem.getItem())) {
                        if (!Material.valueOf(voteItem.getItem()).equals(Material.FURNACE) || !Material.valueOf(voteItem.getItem()).equals(Material.GRINDSTONE)) {
                            if (Arrays.stream(HightableUtility.overworldOres).toList().contains(m)) {
                                int xpDropped = e.getExpToDrop();
                                e.setExpToDrop(getXPBonus(voteItem, xpDropped, p));
                            } else if (Arrays.stream(HightableUtility.netherOres).toList().contains(m)) {
                                int xpDropped = e.getExpToDrop();
                                e.setExpToDrop(getXPBonus(voteItem, xpDropped, p));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        PlayerData data = plugin.getData();
        Entity entity = e.getEntity();
        Player killer = e.getEntity().getKiller();
        EntityType type = entity.getType();

        if (killer != null) {
            ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
            for (VoteItem voteItem : topVotes) {
                String section = voteItem.getSection();
                String item = voteItem.getItem();
                if (section.equals("mobDrops") && HightableUtility.isValidEntity(item) && EntityType.valueOf(item).equals(type)) {
                    List<ItemStack> drops = e.getDrops();
                    for (ItemStack drop : drops) {
                        setItemBonus(voteItem, drop, killer);
                    }
                } else if (section.equals("xpDrops")) {
                    if (Arrays.stream(HightableUtility.passiveMobs).toList().contains(type) ||
                        Arrays.stream(HightableUtility.neutralMobs).toList().contains(type) ||
                        Arrays.stream(HightableUtility.hostileMobs).toList().contains(type)) {
                        int xpDropped = e.getDroppedExp();
                        e.setDroppedExp(getXPBonus(voteItem, xpDropped, killer));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCatchFish(PlayerFishEvent e) {
        PlayerData data = plugin.getData();
        Player p = e.getPlayer();

        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem voteItem : topVotes) {
            String section = voteItem.getSection();
            String item = voteItem.getItem();
            if (section.equals("xpDrops") && item.equals("FISHING_ROD")) {
                int xpDropped = e.getExpToDrop();
                e.setExpToDrop(getXPBonus(voteItem, xpDropped, p));
            }
        }
    }

    @EventHandler
    public void onCookCollect(FurnaceExtractEvent e) {
        PlayerData data = plugin.getData();
        Player p = e.getPlayer();

        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem voteItem : topVotes) {
            String section = voteItem.getSection();
            String item = voteItem.getItem();
            if (section.equals("xpDrops") && item.equals("FURNACE")) {
                int xpDropped = e.getExpToDrop();
                e.setExpToDrop(getXPBonus(voteItem, xpDropped, p));
            }
        }
    }

    @EventHandler
    public void onAnimalBreed(EntityBreedEvent e) {
        PlayerData data = plugin.getData();
        Player p = (Player) e.getBreeder();

        if(p != null) {
            ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
            for (VoteItem voteItem : topVotes) {
                String section = voteItem.getSection();
                String item = voteItem.getItem();
                if (section.equals("xpDrops") && item.equals("WHEAT")) {
                    int xpDropped = e.getExperience();
                    e.setExperience(getXPBonus(voteItem, xpDropped, p));
                }
            }
        }
    }

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        Player p = event.getEnchanter();
        playerOffers.remove(p, event.getOffers());
        playerOffers.put(p, event.getOffers());

        for (VoteItem item : topVotes) {
            if (item.getSection().equals("discounts") && item.getItem().equals("ENCHANTING_TABLE")) {
                double multiplier = Double.parseDouble(item.getMultiplier());
                EnchantmentOffer[] offers = event.getOffers();
                for (EnchantmentOffer offer : offers) {
                    int originalCost = offer.getCost();
                    int newCost = (int) Math.ceil(originalCost * multiplier);
                    offer.setCost(newCost);
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        ItemStack itemToEnchant = event.getItem();
        Player p = event.getEnchanter();

        for (VoteItem item : topVotes) {
            if (item.getSection().equals("discounts") && item.getItem().equals("ENCHANTING_TABLE")) {
                for(Map.Entry<Player, EnchantmentOffer[]> entry : playerOffers.entrySet()) {
                    EnchantmentOffer[] enchantments = entry.getValue();
                    for(EnchantmentOffer enchant : enchantments) {
                        if(enchant.getCost() == event.getExpLevelCost()) {
                            itemToEnchant.addEnchantment(enchant.getEnchantment(), enchant.getEnchantmentLevel());
                            p.sendMessage(UnicodeCharacters.discountedEnchanting(item.getMultiplier()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();

        for (VoteItem item : topVotes) {
            if (item.getSection().equals("discounts") && item.getItem().equals("ANVIL")) {
                double multiplier = Double.parseDouble(item.getMultiplier());
                int originalCost = anvil.getRepairCost();
                int newCost = (int) Math.ceil(originalCost * multiplier);
                anvil.setRepairCost(newCost);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory anvil) {
            if (event.getRawSlot() == 2) {
                ItemStack result = anvil.getItem(2);
                if (result != null && result.getType() != Material.AIR) {
                    Player p = (Player) event.getWhoClicked();
                    PlayerData data = plugin.getData();
                    ArrayList<VoteItem> topVotes = data.getActiveMultipliers();

                    for (VoteItem item : topVotes) {
                        if (item.getSection().equals("discounts") && item.getItem().equals("ANVIL")) {
                            p.sendMessage(UnicodeCharacters.discountedAnvil(item.getMultiplier()));
                        }
                    }
                }
            }
        } else if (event.getInventory() instanceof GrindstoneInventory grindstone && event.getSlotType() == InventoryType.SlotType.RESULT) {
            Player p = (Player) event.getWhoClicked();
            ItemStack resultItem = null;
            for (int i = 0; i <= 1; i++) {
                if (grindstone.getItem(i) != null) {
                    resultItem = grindstone.getItem(i);
                    break;
                }
            }
            if (resultItem != null && !resultItem.getType().isAir()) {
                int totalXP = calculateExperience(resultItem);
                PlayerData data = plugin.getData();
                ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
                for (VoteItem item : topVotes) {
                    if (item.getSection().equals("xpDrops") && item.getItem().equals("GRINDSTONE")) {
                        double multiplier = Double.parseDouble(item.getMultiplier());
                        int finalXP = (int) (totalXP * multiplier);

                        removeNearbyExperienceOrbs(p.getLocation());
                        dropExperience(p.getLocation(), finalXP);

                        p.sendMessage(UnicodeCharacters.extraXPBonus());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent e) {
        Player p = e.getPlayer();
        Location tradeLocation = p.getLocation();
        int xp = 3 + (int) (Math.random() * 4);
        Villager villager = (Villager) e.getVillager();
        if(levels.contains(villager.getVillagerExperience())) {xp += 5;}
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem item : topVotes) {
            double multiplier = Double.parseDouble(item.getMultiplier());
            if (item.getSection().equals("xpDrops") && item.getItem().equals("EMERALD")) {
                int finalXP = (int) (xp * multiplier);
                removeNearbyExperienceOrbs(tradeLocation);
                dropExperience(tradeLocation, finalXP);
                p.sendMessage(UnicodeCharacters.extraXPBonus());
            } else if(item.getSection().equals("discounts") && item.getItem().equals("EMERALD")) {
                p.sendMessage(UnicodeCharacters.discountedTrading(item.getMultiplier()));
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem item : topVotes) {
            if (item.getSection().equals("mobDifficulty") && item.getItem().equals(entity.getType().toString())) {
                double multiplier = Double.parseDouble(item.getMultiplier());
                double newHealth = entity.getHealth() * multiplier;
                AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if(maxHealth != null) {maxHealth.setBaseValue(newHealth);}
                entity.setHealth(newHealth);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();
            if (!originalRecipes.containsKey(merchant)) {
                originalRecipes.put(merchant, new ArrayList<>(merchant.getRecipes()));
            }

            double discountMultiplier = getDiscountMultiplierForPlayer();
            if(discountMultiplier != 1.0) {
                List<MerchantRecipe> discountedRecipes = new ArrayList<>();
                for (MerchantRecipe recipe : merchant.getRecipes()) {
                    discountedRecipes.add(applyDiscountToRecipe(recipe, discountMultiplier));
                }
                merchant.setRecipes(discountedRecipes);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();
            if (originalRecipes.containsKey(merchant)) {
                merchant.setRecipes(originalRecipes.get(merchant));
                originalRecipes.remove(merchant);
            }
        }
    }

    private MerchantRecipe applyDiscountToRecipe(MerchantRecipe originalRecipe, double multiplier) {
        MerchantRecipe newRecipe = new MerchantRecipe(
            originalRecipe.getResult(),
            originalRecipe.getUses(),
            originalRecipe.getMaxUses(),
            originalRecipe.hasExperienceReward(),
            originalRecipe.getVillagerExperience(),
            originalRecipe.getPriceMultiplier()
        );
        List<ItemStack> ingredients = new ArrayList<>();
        for (ItemStack item : originalRecipe.getIngredients()) {
            ItemStack newItem = item.clone();
            newItem.setAmount(Math.max(1, (int) Math.ceil(item.getAmount() * multiplier)));
            ingredients.add(newItem);
        }
        newRecipe.setIngredients(ingredients);
        return newRecipe;
    }

    private double getDiscountMultiplierForPlayer() {
        PlayerData data = plugin.getData();
        ArrayList<VoteItem> topVotes = data.getActiveMultipliers();
        for (VoteItem item : topVotes) {
            if (item.getSection().equals("discounts") && item.getItem().equals("EMERALD")) {
                return Double.parseDouble(item.getMultiplier());
            }
        }
        return 1.0;
    }

    private void removeNearbyExperienceOrbs(Location location) {
        Collection<Entity> nearbyEntities = location.getWorld().getEntitiesByClasses(ExperienceOrb.class);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof ExperienceOrb orb) {
                if (orb.getLocation().distance(location) <= 5) {
                    if (!orb.hasMetadata("CustomXP") && !orb.getPersistentDataContainer().has(new NamespacedKey(plugin, "CustomXP"), PersistentDataType.INTEGER)) {
                        orb.remove();
                    }
                }
            }
        }
    }

    private void dropExperience(Location location, int amount) {
        location.getWorld().spawn(location, ExperienceOrb.class, orb -> {
            orb.setExperience(amount);
            orb.getPersistentDataContainer().set(new NamespacedKey(plugin, "CustomXP"), PersistentDataType.INTEGER, 1);
        });
    }

    private int calculateExperience(ItemStack item) {
        int xp = 0;
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta enchMeta) {
            Map<Enchantment, Integer> storedEnchants = enchMeta.getStoredEnchants();

            for (Map.Entry<Enchantment, Integer> enchantment : storedEnchants.entrySet()) {
                int level = enchantment.getValue();
                int minValue = enchantment.getKey().getMinModifiedCost(level);
                xp += minValue;
            }
        } else {
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
                int level = enchantment.getValue();
                int minValue = enchantment.getKey().getMinModifiedCost(level);
                xp += minValue;
            }
        }
        int minXP = (int) Math.ceil(xp / 2.0);
        int maxXP = xp;
        return minXP + new Random().nextInt(maxXP - minXP + 1);
    }

    private void setItemBonus(VoteItem item, ItemStack drop, Player p) {
        Random random = new Random();
        double rand = random.nextDouble();
        double multiplier = Double.parseDouble(item.getMultiplier());
        double chance = (multiplier - 1) * 100;

        if (rand * 100 < chance) {
            int originalAmount = drop.getAmount();
            int addedAmount = (int) Math.floor(originalAmount * (multiplier));
            drop.setAmount(originalAmount + addedAmount);
            p.sendMessage(UnicodeCharacters.extraItemDrop(addedAmount, drop.getType()));
        }
    }

    private int getXPBonus(VoteItem item, int xpDropped, Player p) {
        Random random = new Random();
        double rand = random.nextDouble();
        double multiplier = Double.parseDouble(item.getMultiplier());
        double chance = (multiplier - 1) * 100;

        if (rand * 100 < chance) {
            p.sendMessage(UnicodeCharacters.extraXPBonus());
            xpDropped = xpDropped + (int) Math.floor(xpDropped * multiplier);
        }
        return xpDropped;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        block.setMetadata("playerPlaced", new FixedMetadataValue(plugin, true));
    }
}
