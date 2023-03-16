package mod.azure.azurelib.helper;

import static java.lang.Math.min;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface Growable {
	float getGrowth();

	void setGrowth(float growth);

	float getMaxGrowth();

	default void grow(LivingEntity entity, float amount) {
		setGrowth(min(getGrowth() + amount, getMaxGrowth()));
		if (getGrowth() >= getMaxGrowth())
			growUp(entity);
	}

	LivingEntity growInto();

	default void growUp(LivingEntity entity) {
		var world = entity.level;
		if (!world.isClientSide()) {
			var newEntity = growInto();
			if (newEntity == null)
				return;
			newEntity.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
			world.addFreshEntity(newEntity);
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	default float getGrowthNeededUntilGrowUp() {
		return getMaxGrowth() - getGrowth();
	}

	default float getGrowthMultiplier() {
		return 1.0f;
	}
}
