package blusunrize.immersiveengineering.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockWoodenDecoration;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderWoodenDecoration implements ISimpleBlockRenderingHandler
{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		GL11.glPushMatrix();
		try{
			if(metadata==0)
			{
				renderer.setRenderBounds(0,0,0, 1,1,1);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
			}
			else if(metadata==1)
			{
				GL11.glTranslatef(-.5f,-.5f,-.5f);
				renderer.setRenderBounds(0,0,.375, .25,1,.625);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
				renderer.setRenderBounds(.75,0,.375, 1,1,.625);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
				renderer.setRenderBounds(-.125,.8125,.4375, 1.125,.9375,.5625);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
				renderer.setRenderBounds(-.125,.3125,.4375, 1.125,.4375,.5625);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
				GL11.glTranslatef(.5f,.5f,.5f);
			}
			else if(metadata==2)
			{
				renderer.setRenderBounds(0,0,0, 1,.5,1);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
			}
			else if(metadata==4)
			{
				renderer.setRenderBounds(0,0,0, 1,1,1);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
			}
			else if(metadata==5)
			{
				renderer.setRenderBounds(0,0,0, 1,1,1);
				ClientUtils.drawInventoryBlock(block, metadata, renderer);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		GL11.glEnable(32826);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if(world.getBlockMetadata(x, y, z)==1)
		{
			renderer.setRenderBounds(.375,0,.375, .625,1,.625);
			renderer.renderStandardBlock(block, x, y, z);
			BlockWoodenDecoration wd = (BlockWoodenDecoration)block;

			if(wd.canConnectFenceTo(world, x+1, y, z))
			{
				renderer.setRenderBounds(.625,.375,.4375, 1,.5625,.5625);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(.625,.75,.4375, 1,.9375,.5625);
				renderer.renderStandardBlock(block, x, y, z);
			}
			if(wd.canConnectFenceTo(world, x-1, y, z))
			{
				renderer.setRenderBounds(0,.375,.4375, .375,.5625,.5625);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0,.75,.4375, .375,.9375,.5625);
				renderer.renderStandardBlock(block, x, y, z);
			}
			if(wd.canConnectFenceTo(world, x, y, z+1))
			{
				renderer.setRenderBounds(.4375,.375,.625, .5625,.5625,1);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(.4375,.75,.625, .5625,.9375,1);
				renderer.renderStandardBlock(block, x, y, z);
			}
			if(wd.canConnectFenceTo(world, x, y, z-1))
			{
				renderer.setRenderBounds(.4375,.375,0, .5625,.5625,.375);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(.4375,.75,0, .5625,.9375,.375);
				renderer.renderStandardBlock(block, x, y, z);
			}
			return true;
		}
		else if(world.getBlockMetadata(x, y, z)==5)
		{
			renderer.setRenderBoundsFromBlock(block);
			renderer.renderFromInside=true;
			renderer.renderMinX+=block.shouldSideBeRendered(world,x-1,y,z,1)?.015625:0;
			renderer.renderMinY+=block.shouldSideBeRendered(world,x,y-1,z,0)?.015625:0;
			renderer.renderMinZ+=block.shouldSideBeRendered(world,x,y,z-1,1)?.015625:0;
			renderer.renderMaxX-=block.shouldSideBeRendered(world,x+1,y,z,1)?.015625:0;
			renderer.renderMaxY-=block.shouldSideBeRendered(world,x,y+1,z,1)?.015625:0;
			renderer.renderMaxZ-=block.shouldSideBeRendered(world,x,y,z+1,1)?.015625:0;
			renderer.renderStandardBlock(block, x, y, z);
			renderer.renderMinX-=block.shouldSideBeRendered(world,x-1,y,z,1)?.015625:0;
			renderer.renderMinY-=block.shouldSideBeRendered(world,x,y-1,z,0)?.015625:0;
			renderer.renderMinZ-=block.shouldSideBeRendered(world,x,y,z-1,1)?.015625:0;
			renderer.renderMaxX+=block.shouldSideBeRendered(world,x+1,y,z,1)?.015625:0;
			renderer.renderMaxY+=block.shouldSideBeRendered(world,x,y+1,z,1)?.015625:0;
			renderer.renderMaxZ+=block.shouldSideBeRendered(world,x,y,z+1,1)?.015625:0;
			renderer.renderFromInside=false;
			return renderer.renderStandardBlock(block, x, y, z);
		}
		else if(world.getBlockMetadata(x, y, z)==6)
		{
			renderer.setRenderBounds(.3125,0,.3125, .6875,.125,.6875);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(0,.125,.3125, .125,.625,.6875);
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setRenderBounds(.125,.125,.375, .625,.25,.625);
			renderer.renderStandardBlock(block, x, y, z);
			Vec3[] vs = {
					Vec3.createVectorHelper(.4375,.25,.4375),
					Vec3.createVectorHelper(.4375,.25,.5625),
					Vec3.createVectorHelper(.5625,.25,.4375),
					Vec3.createVectorHelper(.5625,.25,.5625),
					Vec3.createVectorHelper(.125,.4375,.4375),
					Vec3.createVectorHelper(.125,.4375,.5625),
					Vec3.createVectorHelper(.125,.5625,.4375),
					Vec3.createVectorHelper(.125,.5625,.5625)
			};
			ClientUtils.drawWorldSubBlock(renderer, world, block, x, y, z, vs);
			
			return true;
		}
		else
		{
			renderer.setRenderBoundsFromBlock(block);
			return renderer.renderStandardBlock(block, x, y, z);
		}
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}
	@Override
	public int getRenderId()
	{
		return renderID;
	}

}
