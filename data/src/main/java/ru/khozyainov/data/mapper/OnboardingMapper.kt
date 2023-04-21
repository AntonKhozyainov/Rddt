package ru.khozyainov.data.mapper

import ru.khozyainov.data.base.EntityMapper
import ru.khozyainov.data.models.OnboardingEntity
import ru.khozyainov.domain.model.Onboarding

class OnboardingMapper : EntityMapper<Onboarding, OnboardingEntity> {

    override fun mapToDomain(entity: OnboardingEntity): Onboarding = Onboarding(
        viewed = entity.viewed
    )

    override fun mapToEntity(model: Onboarding): OnboardingEntity = OnboardingEntity(
        viewed = model.viewed
    )
}