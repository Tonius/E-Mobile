package tonius.emobile.gui;

import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tonius.emobile.EMobile;
import tonius.emobile.config.EMConfig;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.MessageCellphoneAuthorize;
import tonius.emobile.network.message.MessageCellphoneCancel;
import tonius.emobile.network.message.MessageCellphoneHome;
import tonius.emobile.network.message.MessageCellphonePlayer;
import tonius.emobile.network.message.MessageCellphoneSpawn;
import tonius.emobile.util.StringUtils;

public class GuiCellphone extends GuiContainerBase {

    private GuiTextField accept;
    private GuiTextField receiver;
    private GuiButton buttonAccept;
    private GuiButton buttonReceiver;
    private GuiButton buttonHome;
    private GuiButton buttonSpawn;
    private GuiButton buttonCancel;

    public GuiCellphone(ContainerCellphone cellphone) {
        super(cellphone);
        this.xSize = 176;
        this.ySize = 203;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.accept = new GuiTextField(this.fontRendererObj, this.guiLeft + 8, this.guiTop + 41, 127, 12);
        this.receiver = new GuiTextField(this.fontRendererObj, this.guiLeft + 8, this.guiTop + 71, 127, 12);

        this.accept.setEnabled(EMConfig.allowTeleportPlayers);
        this.accept.setVisible(EMConfig.allowTeleportPlayers);
        this.receiver.setEnabled(EMConfig.allowTeleportPlayers);
        this.receiver.setVisible(EMConfig.allowTeleportPlayers);

        this.buttonAccept = new GuiButtonSmall(0, this.guiLeft + 139, this.guiTop + 40, 30, 14, "OK");
        this.buttonAccept.visible = this.buttonAccept.enabled = EMConfig.allowTeleportPlayers;
        this.buttonList.add(this.buttonAccept);

        this.buttonReceiver = new GuiButtonSmall(1, this.guiLeft + 139, this.guiTop + 70, 30, 14, "OK");
        this.buttonReceiver.visible = this.buttonReceiver.enabled = EMConfig.allowTeleportPlayers;
        this.buttonList.add(this.buttonReceiver);

        this.buttonHome = new GuiButtonSmall(2, this.guiLeft + 7, this.guiTop + 89, 16, 16, "H");
        this.buttonHome.visible = this.buttonHome.enabled = EMConfig.allowTeleportHome;
        this.buttonList.add(this.buttonHome);

        this.buttonSpawn = new GuiButtonSmall(3, this.guiLeft + (EMConfig.allowTeleportHome ? 25 : 7), this.guiTop + 89, 16, 16, "S");
        this.buttonSpawn.visible = this.buttonSpawn.enabled = EMConfig.allowTeleportSpawn;
        this.buttonList.add(this.buttonSpawn);

        this.buttonCancel = new GuiButtonSmall(4, this.guiLeft + 153, this.guiTop + 89, 16, 16, "X");
        this.buttonList.add(this.buttonCancel);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        this.fontRendererObj.drawString(EMobile.cellphone.getItemStackDisplayName(null), 8, 6, 4210752);
        if (EMConfig.allowTeleportPlayers) {
            this.fontRendererObj.drawString(StringUtils.translate("gui.cellphone.accept"), 8, 30, 4210752);
            this.fontRendererObj.drawString(StringUtils.translate("gui.cellphone.teleport"), 8, 60, 4210752);
        }
        this.fontRendererObj.drawString(StringUtils.translate("container.inventory", false), 8, this.ySize - 93, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("emobile", "/textures/gui/item/cellphone.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);

        if (this.accept.getVisible())
            this.accept.drawTextBox();
        if (this.receiver.getVisible())
            this.receiver.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (EMConfig.allowTeleportPlayers) {
            this.accept.updateCursorCounter();
            this.receiver.updateCursorCounter();
        }
    }

    @Override
    protected void getTooltipLines(List lines, int mouseX, int mouseY) {
        if (this.func_146978_c(8, 90, 14, 14, mouseX, mouseY) && this.buttonHome.visible) {
            lines.add(StringUtils.translate("gui.cellphone.home"));
        } else if (this.func_146978_c((EMConfig.allowTeleportHome ? 26 : 8), 90, 14, 14, mouseX, mouseY) && this.buttonSpawn.visible) {
            lines.add(StringUtils.translate("gui.cellphone.spawn"));
        } else if (this.func_146978_c(154, 90, 14, 14, mouseX, mouseY)) {
            lines.add(StringUtils.translate("gui.cellphone.cancel"));
        } else if (this.func_146978_c(152, 8, 16, 16, mouseX, mouseY) && this.inventorySlots.getInventory().get(0) == null && this.mc.thePlayer.inventory.getItemStack() == null) {
            lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.1"));
            lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.2"));
        } else if (this.func_146978_c(8, 41, 127, 12, mouseX, mouseY) && EMConfig.allowTeleportPlayers) {
            lines.add(StringUtils.BOLD + StringUtils.translate("gui.cellphone.prefixes.1"));
            lines.add(StringUtils.BRIGHT_GREEN + "p:" + StringUtils.END + " - " + StringUtils.translate("gui.cellphone.prefixes.2"));
            lines.add("      " + StringUtils.translate("gui.cellphone.prefixes.3"));
            lines.add(StringUtils.BRIGHT_GREEN + "!" + StringUtils.END + " - " + StringUtils.translate("gui.cellphone.prefixes.4"));
            lines.add("      " + StringUtils.translate("gui.cellphone.prefixes.5"));
            lines.add(StringUtils.LIGHT_RED + StringUtils.ITALIC + StringUtils.translate("gui.cellphone.prefixes.6"));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (EMConfig.allowTeleportPlayers) {
            this.accept.mouseClicked(mouseX, mouseY, button);
            this.receiver.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void keyTyped(char c, int i) {
        if (EMConfig.allowTeleportPlayers && (this.accept.isFocused() || this.receiver.isFocused())) {
            if (i == Keyboard.KEY_RETURN) {
                if (this.receiver.isFocused()) {
                    this.requestPlayerTeleport();
                } else if (this.accept.isFocused()) {
                    this.acceptPlayer();
                }
            } else {
                this.accept.textboxKeyTyped(c, i);
                this.receiver.textboxKeyTyped(c, i);
            }
            if (i != Keyboard.KEY_RETURN && i != Keyboard.KEY_BACK && i != Keyboard.KEY_LSHIFT && i != Keyboard.KEY_RSHIFT && i != Keyboard.KEY_LCONTROL && i != Keyboard.KEY_RCONTROL)
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("emobile", "phone")));
        } else {
            super.keyTyped(c, i);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
        case 0:
            this.acceptPlayer();
            break;
        case 1:
            this.requestPlayerTeleport();
            break;
        case 2:
            this.requestHomeTeleport();
            break;
        case 3:
            this.requestSpawnTeleport();
            break;
        case 4:
            this.cancelSessions();
        }
    }

    private int getPearls() {
        ItemStack pearls = (ItemStack) this.inventorySlots.getInventory().get(0);
        return pearls != null && pearls.getItem() instanceof ItemEnderPearl ? pearls.stackSize : 0;
    }

    private void requestPlayerTeleport() {
        if (!this.receiver.getText().equals("") && this.getPearls() > 0) {
            this.mc.thePlayer.closeScreen();
            PacketHandler.instance.sendToServer(new MessageCellphonePlayer(this.mc.thePlayer.getCommandSenderName(), this.receiver.getText()));
        }
    }

    private void acceptPlayer() {
        if (!this.accept.getText().equals("")) {
            this.mc.thePlayer.closeScreen();
            PacketHandler.instance.sendToServer(new MessageCellphoneAuthorize(this.mc.thePlayer.getCommandSenderName(), this.accept.getText()));
        }
    }

    private void requestSpawnTeleport() {
        if (this.getPearls() > 0) {
            this.mc.thePlayer.closeScreen();
            PacketHandler.instance.sendToServer(new MessageCellphoneSpawn(this.mc.thePlayer.getCommandSenderName()));
        }
    }

    private void requestHomeTeleport() {
        if (this.getPearls() > 0) {
            this.mc.thePlayer.closeScreen();
            PacketHandler.instance.sendToServer(new MessageCellphoneHome(this.mc.thePlayer.getCommandSenderName()));
        }
    }

    private void cancelSessions() {
        this.mc.thePlayer.closeScreen();
        PacketHandler.instance.sendToServer(new MessageCellphoneCancel(this.mc.thePlayer.getCommandSenderName()));
    }

}
