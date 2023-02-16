package net.coderbot.iris.gui.option;

import com.mojang.serialization.Codec;
import net.coderbot.iris.Iris;
import net.coderbot.iris.colorspace.ColorSpace;
import net.coderbot.iris.pipeline.WorldRenderingPipeline;
import net.coderbot.iris.pipeline.newshader.NewWorldRenderingPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.io.IOException;
import java.util.List;

public class IrisVideoSettings {
	public static int shadowDistance = 32;
	public static ColorSpace colorSpace = ColorSpace.SRGB;

	// TODO: Tell the user to check in the shader options once that's supported.
	private static final Component DISABLED_TOOLTIP = Component.translatable("options.iris.shadowDistance.disabled");
	private static final Component ENABLED_TOOLTIP = Component.translatable("options.iris.shadowDistance.enabled");

	public static int getOverriddenShadowDistance(int base) {
		return Iris.getPipelineManager().getPipeline()
				.map(pipeline -> pipeline.getForcedShadowRenderDistanceChunksForDisplay().orElse(base))
				.orElse(base);
	}

	public static boolean isShadowDistanceSliderEnabled() {
		return Iris.getPipelineManager().getPipeline()
				.map(pipeline -> !pipeline.getForcedShadowRenderDistanceChunksForDisplay().isPresent())
				.orElse(true);
	}

	public static void colorSpaceChanged() {
		Iris.getPipelineManager().getPipeline()
				.filter(pipeline -> pipeline instanceof NewWorldRenderingPipeline).ifPresent(pipeline -> ((NewWorldRenderingPipeline) pipeline).colorSpaceConverter.changeCurrentColorSpace(colorSpace));
	}

	public static final OptionInstance<Integer> RENDER_DISTANCE = new ShadowDistanceOption<>("options.iris.shadowDistance",
		mc -> {
			WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

			Component tooltip;

			if (pipeline != null) {
				if (pipeline.getForcedShadowRenderDistanceChunksForDisplay().isPresent()) {
					tooltip = DISABLED_TOOLTIP;
				} else {
					tooltip = ENABLED_TOOLTIP;
				}
			} else {
				tooltip = ENABLED_TOOLTIP;
			}

			return aDouble -> Minecraft.getInstance().font.split(tooltip, 200);
		},
		(arg, d) -> {
			WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

			if (pipeline != null) {
				d = pipeline.getForcedShadowRenderDistanceChunksForDisplay().orElse(d);
			}

			if (d <= 0.0) {
				return Component.translatable("options.generic_value", Component.translatable("options.iris.shadowDistance"), "0 (disabled)");
			} else {
				return Component.translatable("options.generic_value",
					Component.translatable("options.iris.shadowDistance"),
					Component.translatable("options.chunks", d));
			}
		},
		new OptionInstance.IntRange(0, 32),
		getOverriddenShadowDistance(shadowDistance),
		integer -> {
			shadowDistance = integer;
			try {
				Iris.getIrisConfig().save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
}
