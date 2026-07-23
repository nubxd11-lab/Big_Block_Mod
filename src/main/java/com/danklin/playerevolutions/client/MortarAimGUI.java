package com.danklin.playerevolutions.client;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.network.PacketFireMortar;
import com.danklin.playerevolutions.tileentities.MortarTileEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class MortarAimGUI extends Screen {

    private final MortarTileEntity mortar;
    private int distance = 20;
    private int yaw = 0;

    public MortarAimGUI(MortarTileEntity mortar) {
        super(new StringTextComponent("Mortar Aiming System"));
        this.mortar = mortar;
        if (mortar != null) {
            this.distance = (int) mortar.getTargetDistance();
            this.yaw = (int) mortar.getTargetYaw();
        }
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addButton(new Button(centerX - 80, centerY - 30, 35, 20, "-5", b -> {
            this.distance = Math.max(5, this.distance - 5);
        }));

        this.addButton(new Button(centerX + 45, centerY - 30, 35, 20, "+5", b -> {
            this.distance = Math.min(100, this.distance + 5);
        }));

        this.addButton(new Button(centerX - 80, centerY + 10, 35, 20, "-15°", b -> {
            this.yaw = (this.yaw - 15 + 360) % 360;
        }));

        this.addButton(new Button(centerX + 45, centerY + 10, 35, 20, "+15°", b -> {
            this.yaw = (this.yaw + 15) % 360;
        }));

        this.addButton(new Button(centerX - 40, centerY + 50, 80, 20, "FIRE!", b -> {
            if (this.mortar != null) {
                PlayerEvolutions.NETWORK.sendToServer(
                        new PacketFireMortar(this.mortar.getPos(), this.distance, this.yaw)
                );
            }
            this.onClose();
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        drawCenteredString(this.font, "MORTAR TARGETING", centerX, centerY - 60, 0xFFFFFF);
        drawCenteredString(this.font, "Range: " + this.distance + " Blocks", centerX, centerY - 25, 0xFFFF55);
        drawCenteredString(this.font, "Yaw: " + this.yaw + "°", centerX, centerY + 15, 0x55FF55);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}