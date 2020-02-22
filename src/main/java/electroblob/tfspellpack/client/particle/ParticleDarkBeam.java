package electroblob.tfspellpack.client.particle;

import electroblob.wizardry.client.particle.ParticleBeam;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticleDarkBeam extends ParticleBeam {

	/** Half the width of the outermost layer. */
	private static final float THICKNESS = 0.1f;

	public ParticleDarkBeam(World world, double x, double y, double z){
		super(world, x, y, z);
	}

	@Override
	protected void draw(Tessellator tessellator, double length, float partialTicks){

		float scale = this.particleScale;

		if(this.particleMaxAge > 0){
			float ageFraction = (particleAge + partialTicks - 1)/particleMaxAge;
			// Squaring this makes it look smoother than a linear shrinking effect
			scale = this.particleScale * (1 - ageFraction*ageFraction);
		}

		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

		for(int layer=0; layer<3; layer++){
			drawSegment(tessellator, layer, 0, 0, 0, 0, 0, length, THICKNESS * scale);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
	}

	/** Draws the given layer of a segment of the arc, from the point (x1, y1, z1) to the point (x2, y2, z2), with the given thickness. */
	private void drawSegment(Tessellator tessellator, int layer, double x1, double y1, double z1, double x2, double y2, double z2, float thickness){

		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		switch(layer){

			case 0:
				drawShearedBox(buffer, x1, y1, z1, x2, y2, z2, 0.25f*thickness, 0, 0, 0, 1);
				break;

			case 1:
				drawShearedBox(buffer, x1, y1, z1, x2, y2, z2, 0.6f*thickness, particleRed/2, particleGreen/2,
						particleBlue/2, 0.65f);
				break;

			case 2:
				drawShearedBox(buffer, x1, y1, z1, x2, y2, z2, thickness, particleRed, particleGreen, particleBlue, 0.3f);
				break;
		}

		tessellator.draw();
	}

	/** Draws a single box for one segment of the arc, from the point (x1, y1, z1) to the point (x2, y2, z2), with given width and colour. */
	private void drawShearedBox(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2, float width, float r, float g, float b, float a){

		buffer.pos(x1-width, y1-width, z1).color(r, g, b, a).endVertex();
		buffer.pos(x2-width, y2-width, z2).color(r, g, b, a).endVertex();
		buffer.pos(x1-width, y1+width, z1).color(r, g, b, a).endVertex();
		buffer.pos(x2-width, y2+width, z2).color(r, g, b, a).endVertex();
		buffer.pos(x1+width, y1+width, z1).color(r, g, b, a).endVertex();
		buffer.pos(x2+width, y2+width, z2).color(r, g, b, a).endVertex();
		buffer.pos(x1+width, y1-width, z1).color(r, g, b, a).endVertex();
		buffer.pos(x2+width, y2-width, z2).color(r, g, b, a).endVertex();
		buffer.pos(x1-width, y1-width, z1).color(r, g, b, a).endVertex();
		buffer.pos(x2-width, y2-width, z2).color(r, g, b, a).endVertex();
	}

}
