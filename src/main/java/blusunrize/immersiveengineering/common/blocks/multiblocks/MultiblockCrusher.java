package blusunrize.immersiveengineering.common.blocks.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalMultiblocks;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;

public class MultiblockCrusher implements IMultiblock
{
	public static MultiblockCrusher instance = new MultiblockCrusher();
	static ItemStack[][][] structure = new ItemStack[3][5][3];
	static{
		for(int h=0;h<3;h++)
			for(int l=0;l<5;l++)
				for(int w=0;w<3;w++)
				{
					if((l==0&&h==2)||(l==4&&h==2)||(l==4&&w==2&&h>0))
						continue;
					if(l==0)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration,1,BlockMetalDecoration.META_lightEngineering);
					else if(l==4)
					{
						if(h<1)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration,1,BlockMetalDecoration.META_scaffolding);
						else if(h<2&&w<2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration,1,BlockMetalDecoration.META_lightEngineering);
					}
					else if(h==0)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration,1, (l==2&&(w==0||w==1))?BlockMetalDecoration.META_lightEngineering: BlockMetalDecoration.META_scaffolding);
					else if(h==1)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration,1, (w==1&&l==2)?BlockMetalDecoration.META_lightEngineering: BlockMetalDecoration.META_fence);
					else if(h==2)
						structure[h][l][w] = new ItemStack(Blocks.hopper);
				}
	}
	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	public boolean isBlockTrigger(Block b, int meta)
	{
		return b==IEContent.blockMetalDecoration && (meta==BlockMetalDecoration.META_fence);
	}

	@Override
	public boolean createStructure(World world, int x, int y, int z, int side, EntityPlayer player)
	{
		if(side==0||side==1)
			return false;
		int startX=x;
		int startY=y;
		int startZ=z;

		if(world.getBlock(x,y-1,z).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(x,y-1,z)==BlockMetalDecoration.META_scaffolding
				&& world.getBlock(x+(side==4?2:side==5?-2:0),y-1,z+(side==2?2:side==3?-2:0)).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(x+(side==4?2:side==5?-2:0),y-1,z+(side==2?2:side==3?-2:0))==BlockMetalDecoration.META_lightEngineering)
		{
			startX = x+(side==4?2:side==5?-2:0);
			startZ = z+(side==2?2:side==3?-2:0);
			side = ForgeDirection.OPPOSITES[side];
		}

		boolean mirrored = false;
		boolean b = structureCheck(world,startX,startY,startZ, side, mirrored);
		if(!b)
		{
			mirrored = true;
			b = structureCheck(world,startX,startY,startZ, side, mirrored);
		}
		
		if(b)
			for(int l=0;l<3;l++)
				for(int w=-2;w<=2;w++)
					for(int h=-1;h<=1;h++)
					{
						if((w==-2&&h==1)||(w==2&&h==1)||(w==2&&l==2&&h>-1))
							continue;
						int ww = mirrored?-w:w;
						int xx = startX+ (side==4?l: side==5?-l: side==2?-ww : ww);
						int yy = startY+ h;
						int zz = startZ+ (side==2?l: side==3?-l: side==5?-ww : ww);

						world.setBlock(xx, yy, zz, IEContent.blockMetalMultiblocks, BlockMetalMultiblocks.META_crusher, 0x3);
						if(world.getTileEntity(xx, yy, zz) instanceof TileEntityCrusher)
						{
							TileEntityCrusher tile = (TileEntityCrusher)world.getTileEntity(xx,yy,zz);
							tile.facing=side;
							tile.formed=true;
							tile.pos = l*15 + (h+1)*5 + (w+2);
							tile.offset = new int[]{(side==4?l-1: side==5?1-l: side==2?-ww: ww),h+1,(side==2?l-1: side==3?1-l: side==5?-ww: ww)};
							tile.mirrored = mirrored;
						}
					}
		return b;
	}

	boolean structureCheck(World world, int startX, int startY, int startZ, int side, boolean mirror)
	{
		for(int l=0;l<3;l++)
			for(int w=-2;w<=2;w++)
				for(int h=-1;h<=1;h++)
				{
					if((w==-2&&h==1)||(w==2&&h==1)||(w==2&&l==2&&h>-1))
						continue;
					int ww = mirror?-w:w;
					int xx = startX+ (side==4?l: side==5?-l: side==2?-ww : ww);
					int yy = startY+ h;
					int zz = startZ+ (side==2?l: side==3?-l: side==5?-ww : ww);

					if(w==-2)
					{
						if(!(world.getBlock(xx, yy, zz).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(xx, yy, zz)==BlockMetalDecoration.META_lightEngineering))
						{
							System.out.println("break on "+xx+","+yy+","+zz+"; missing 0");
							return false;
						}
					}
					else if(w==2 && h==0)
					{
						if(l<2 && !(world.getBlock(xx, yy, zz).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(xx, yy, zz)==BlockMetalDecoration.META_lightEngineering))
						{
							System.out.println("break on "+xx+","+yy+","+zz+"; missing 1");
							return false;
						}
					}
					else if(h==-1)
					{
						if(!(world.getBlock(xx, yy, zz).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(xx, yy, zz)==((w==0&&(l==0||l==1))?BlockMetalDecoration.META_lightEngineering: BlockMetalDecoration.META_scaffolding) ))
						{
							System.out.println("break on "+xx+","+yy+","+zz+"; missing 2");
							return false;
						}
					}
					else if(h==0)
					{
						if(!(world.getBlock(xx, yy, zz).equals(IEContent.blockMetalDecoration) && world.getBlockMetadata(xx, yy, zz)==((l==1&&w==0)?BlockMetalDecoration.META_lightEngineering: BlockMetalDecoration.META_fence) ))
						{
							System.out.println("break on "+xx+","+yy+","+zz+"; missing 3");
							return false;
						}
					}
					else if(h==1)
					{
						if(!world.getBlock(xx, yy, zz).equals(Blocks.hopper))
						{
							System.out.println("break on "+xx+","+yy+","+zz+"; missing 4");
							return false;
						}
					}
				}
		return true;
	}

}