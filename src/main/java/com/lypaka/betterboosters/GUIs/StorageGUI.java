package com.lypaka.betterboosters.GUIs;

import ca.landonjw.gooeylibs2.api.button.ButtonClick;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class StorageGUI {

    public static void open (ServerPlayerEntity player) throws ObjectMappingException {

        Account account = AccountHandler.accountMap.get(player.getUniqueID());
        if (account.getStoredMap().isEmpty()) {

            player.sendMessage(FancyText.getFormattedText("&eYou have no stored Boosters to display!"), player.getUniqueID());
            return;

        }

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.storageGUIRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedText(ConfigGetters.storageGUITitle))
                .build();

        String[] borderSlots = ConfigGetters.storageGUIBorderSlots.split(", ");
        for (String b : borderSlots) {

            page.getTemplate().getSlot(Integer.parseInt(b)).setButton(GooeyButton.builder().display(getBorder()).build());

        }

        Map<String, Map<String, String>> slots = BetterBoosters.configManager.getConfigNode(1, "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        for (Map.Entry<String, Map<String, String>> entry : slots.entrySet()) {

            if (entry.getKey().contains("Slot")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String boosterName = data.get("Booster");
                Booster booster = BoosterUtils.getFromName(boosterName);
                List<String> loreString = BetterBoosters.configManager.getConfigNode(1, "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                if (booster != null) {

                    int amount = 0;
                    if (account.getStoredMap().containsKey(booster.getName())) {

                        amount = account.getStoredMap().get(booster.getName());

                    }

                    ItemStack boosterStack = BoosterUtils.buildBoosterItem(booster, 1);
                    ListNBT lore = new ListNBT();
                    for (String s : loreString) {

                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s.replace("%amount%", String.valueOf(amount))))));

                    }
                    boosterStack.getOrCreateChildTag("display").put("Lore", lore);

                    if (amount > 0) {

                        int finalAmount = amount;
                        page.getTemplate().getSlot(slot).setButton(
                                GooeyButton.builder()
                                        .display(boosterStack)
                                        .onClick(click -> {

                                            int toWithdraw = 0;
                                            if (click.getClickType() == ButtonClick.LEFT_CLICK) {

                                                toWithdraw = 1;

                                            } else if (click.getClickType() == ButtonClick.SHIFT_LEFT_CLICK) {

                                                toWithdraw = finalAmount;

                                            }

                                            if (toWithdraw > 0) {

                                                int current = account.getStoredMap().get(booster.getName());
                                                int updated = current - toWithdraw;
                                                if (updated == 0) {

                                                    account.getStoredMap().remove(booster.getName());

                                                } else {

                                                    account.getStoredMap().put(booster.getName(), updated);

                                                }

                                                ItemStack boosterToWithdraw = BoosterUtils.buildBoosterItem(booster, toWithdraw);
                                                player.addItemStackToInventory(boosterToWithdraw);
                                                String message = ConfigGetters.messagesMap.get("Booster-Withdrew");
                                                if (toWithdraw == 1) {

                                                    message = message.replace("Boosters", "Booster");

                                                }
                                                message = message.replace("%booster%", booster.getName());
                                                player.sendMessage(FancyText.getFormattedText(message), player.getUniqueID());
                                                try {

                                                    open(player);

                                                } catch (ObjectMappingException e) {

                                                    e.printStackTrace();

                                                }

                                            }

                                        })
                                        .build()
                        );

                    } else {

                        page.getTemplate().getSlot(slot).setButton(
                                GooeyButton.builder()
                                        .display(boosterStack)
                                        .build()
                        );

                    }

                }

            }

        }

    }

    private static ItemStack getBorder() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.storageGUIBorderID);
        item.setDisplayName(FancyText.getFormattedText(""));
        return item;

    }

}
