package ru.khozyainov.rddt.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.ItemOnboardingBinding
import ru.khozyainov.rddt.model.OnboardingItem

class OnboardingAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<OnboardingAdapter.Holder>(), View.OnClickListener {

    private val onboardingList = listOf(
        OnboardingItem(
            image = R.drawable.onboarding_welcome,
            title = R.string.title_onboarding_1,
            description = R.string.description_onboarding_1
        ), OnboardingItem(
            image = R.drawable.onboarding_sub_rddt,
            title = R.string.title_onboarding_2,
            description = R.string.description_onboarding_2
        ), OnboardingItem(
            image = R.drawable.onboarding_share_and_save,
            title = R.string.title_onboarding_3,
            description = R.string.description_onboarding_3
        )
    )

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nextIconButton -> listener.showNextPage()
            R.id.onboardingDoneButton -> listener.onboardingComplete()
            else -> listener.showPrevPage()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        with(binding) {
            nextIconButton.setOnClickListener(this@OnboardingAdapter)
            backIconButton.setOnClickListener(this@OnboardingAdapter)
            onboardingDoneButton.setOnClickListener(this@OnboardingAdapter)
        }

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.binding) {
            backIconButton.isVisible = position != 0
            nextIconButton.isVisible = position != 2
            onboardingDoneButton.isVisible = position == 2
            onboardingTitleTextView.setText(onboardingList[position].title)
            onboardingDescriptionTextView.setText(onboardingList[position].description)
            onboardingImageView.setImageResource(onboardingList[position].image)
        }
    }

    override fun getItemCount(): Int = onboardingList.size

    class Holder(
        val binding: ItemOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun showNextPage()
        fun showPrevPage()
        fun onboardingComplete()
    }
}