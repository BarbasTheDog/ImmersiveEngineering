package blusunrize.immersiveengineering.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;

public class TileRenderBucketWheel extends TileEntitySpecialRenderer
{
	static IModelCustom objmodel = ClientUtils.getModel("immersiveengineering:models/bucketWheel.obj");

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
	{
		TileEntityBucketWheel wheel = (TileEntityBucketWheel)tile;
//		if(!wheel.formed || crusher.pos!=17)
//			return;
		GL11.glPushMatrix();

		GL11.glTranslated(x+.5, y+.5, z+.5);
//		GL11.glRotatef(crusher.facing==2?180: crusher.facing==4?-90: crusher.facing==5?90: 0, 0,1,0);

//		if(crusher.mirrored)
//		{
//			GL11.glScalef(-1,1,1);
//			GL11.glDisable(GL11.GL_CULL_FACE);
//		}

		ClientUtils.bindTexture("immersiveengineering:textures/models/bucketWheel.png");

//		boolean b = (crusher.active&&crusher.process>0)||crusher.mobGrinding||crusher.grindingTimer>0;
//		objmodel.renderAllExcept("drum0","drum1");
		objmodel.renderAll();

//		float angle = crusher.barrelRotation+(b?18*f:0);
//		
//		GL11.glTranslated(17/16f,14/16f,-8.5/16f);
//		GL11.glRotatef(angle, 1,0,0);
//		objmodel.renderOnly("drum1");
//		GL11.glRotatef(-angle, 1,0,0);
//		GL11.glTranslated(0,0,17/16f);
//		GL11.glRotatef(-angle, 1,0,0);
//		objmodel.renderOnly("drum0");
//		
//		if(crusher.mirrored)
//		{
//			GL11.glScalef(-1,1,1);
//			GL11.glEnable(GL11.GL_CULL_FACE);
//		}
		GL11.glPopMatrix();
	}

}