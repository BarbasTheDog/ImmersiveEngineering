package blusunrize.immersiveengineering.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author BluSunrize - 03.06.2015
 *
 * The Handler for the Excavator. Chunk->Ore calculation is done here, as is registration
 */
public class ExcavatorHandler
{
	/**
	 * A HashMap of MineralMixes and their rarity (Integer out of 100)
	 */
	public static LinkedHashMap<MineralMix, Integer> mineralList = new LinkedHashMap<MineralMix, Integer>();
	public static HashMap<DimensionChunkCoords, Integer> mineralDepletion = new HashMap<DimensionChunkCoords, Integer>();
	public static int totalWeight = 0;
	public static int mineralVeinCapacity = 0;

	public static void addMineral(String name, int mineralChance, float failChance, String[] ores, float[] chances)
	{
		assert ores.length == chances.length;
		mineralList.put(new MineralMix(name, failChance, ores, chances), mineralChance);
	}
	public static void recalculateChances()
	{
		totalWeight = 0;
		for(Map.Entry<MineralMix, Integer> e : mineralList.entrySet())
		{
			totalWeight += e.getValue();
			e.getKey().recalculateChances();
		}
	}


	public static MineralMix getRandomMineral(World world, int chunkX, int chunkZ)
	{
		if(world.isRemote)
			return null;

		if(!mineralDepletion.containsKey(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ)))
			mineralDepletion.put(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ), 0);
		int dep = mineralDepletion.get(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ));
		if(dep>mineralVeinCapacity)
			return null;

		long seed = world.getSeed();
		boolean empty = ((seed+(chunkX*chunkX + chunkZ*chunkZ))^seed)%8!=0; //Used to be 1 in 4
		int query = (int) ((seed+((chunkX*chunkX*71862)+(chunkZ*chunkZ*31261)))^seed);
		if(empty)
			return null;
		int weight = query%totalWeight;

		for(Map.Entry<MineralMix, Integer> e : mineralList.entrySet())
		{
			weight -= e.getValue();
			if(weight < 0)
				return e.getKey();
		}
		return null;
	}
	public static void depleteMinerals(World world, int chunkX, int chunkZ)
	{
		if(!mineralDepletion.containsKey(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ)))
			mineralDepletion.put(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ), 0);
		int dep = mineralDepletion.get(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ));
		mineralDepletion.put(new DimensionChunkCoords(world.provider.dimensionId, chunkX,chunkZ), dep+1);
	}

	public static class MineralMix
	{
		public String name;
		public float failChance;
		public String[] ores;
		public float[] chances;
		public String[] recalculatedOres;
		public float[] recalculatedChances;

		public MineralMix(String name, float failChance, String[] ores, float[] chances)
		{
			this.name = name;
			this.failChance = failChance;
			this.ores = ores;
			this.chances = chances;

			recalculateChances();
		}

		public void recalculateChances()
		{
			float chanceSum = 0;
			ArrayList<String> existing = new ArrayList();
			for(int i=0; i<ores.length; i++)
				if(OreDictionary.getOres(ores[i]).size()>0)
				{
					existing.add(ores[i]);
					chanceSum += chances[i];
				}
			recalculatedOres = existing.toArray(new String[0]);
			recalculatedChances = new float[existing.size()];
			int j=0;
			for(int i=0; i<ores.length; i++)
				if(OreDictionary.getOres(ores[i]).size()>0)
					this.recalculatedChances[j++] = chances[i]/chanceSum;
		}

		public String getRandomOre(Random rand)
		{
			float r = rand.nextFloat();
			for(int i=0; i<chances.length; i++)
			{
				r -= chances[i];
				if(r < 0)
					return this.ores[i];
			}
			return "";
		}
	}
}