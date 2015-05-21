package blusunrize.immersiveengineering.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import blusunrize.immersiveengineering.client.models.ModelIEObj;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

public class TileRenderConnectorStructural extends TileRenderIE
{
	static ModelIEObj model = new ModelIEObj("immersiveengineering:models/connectorStructural.obj")
	{
		@Override
		public IIcon getBlockIcon()
		{
			return IEContent.blockMetalDecoration.getIcon(0, BlockMetalDecoration.META_connectorStrutural);
		}
	};

	@Override
	public void renderDynamic(TileEntity tile, double x, double y, double z, float f)
	{
	}

	@Override
	public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix)
	{
		translationMatrix.translate(.5, .5, .5);

		TileEntityConnectorLV connector = (TileEntityConnectorLV)tile;
		switch(connector.facing)
		{
		case 0:
			break;
		case 1:
			rotationMatrix.rotate(Math.toRadians(180), 0,0,1);
			break;
		case 2:
			rotationMatrix.rotate(Math.toRadians(90), 1,0,0);
			break;
		case 3:
			rotationMatrix.rotate(Math.toRadians(-90), 1,0,0);
			break;
		case 4:
			rotationMatrix.rotate(Math.toRadians(-90), 0,0,1);
			break;
		case 5:
			rotationMatrix.rotate(Math.toRadians(90), 0,0,1);
			break;
		}

		model.render(tile, tes, translationMatrix, rotationMatrix, false);
	}

}