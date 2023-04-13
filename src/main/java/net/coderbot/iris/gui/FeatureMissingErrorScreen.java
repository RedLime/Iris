package net.coderbot.iris.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

public class FeatureMissingErrorScreen extends Screen {
	private final Screen parent;
	private MutableComponent message;
	private final FormattedText messageTemp;

	public FeatureMissingErrorScreen(Screen parent, Component title, Component message) {
		super(title);
		this.parent = parent;
		this.messageTemp = message;
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new Button(this.width / 2 - 100, 140, 200, 20, CommonComponents.GUI_BACK, arg -> this.minecraft.setScreen(parent)));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
		this.renderBackground(poseStack);
		this.drawCenteredString(poseStack, this.font, this.title, this.width / 2, 90, 0xFFFFFF);
		this.drawCenteredString(poseStack, this.font, this.messageTemp, this.width / 2, 110, 0xFFFFFF);
		super.render(poseStack, mouseX, mouseY, delta);
	}
}
