package net.coderbot.iris.mixin;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Suppresses Minecraft's authentication check in development environments. It's unnecessary log spam, and there's no
 * need to send off a network request to Microsoft telling them that we're using Fabric/Quilt every time we launch the
 * game in the development environment.
 */
@Mixin(Minecraft.class)
public class MixinMinecraft_NoAuthInDev {
	@Shadow
	@Final
	private static Logger LOGGER;
}
