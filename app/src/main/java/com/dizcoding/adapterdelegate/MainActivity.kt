package com.dizcoding.adapterdelegate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dizcoding.adapterdelegate.databinding.ActivityMainBinding
import com.dizcoding.adapterdelegate.databinding.SampleOneMenuBinding
import com.dizcoding.adapterdelegate.databinding.SampleOneMenusBinding
import com.dizcoding.adapterdelegate.databinding.SampleOneUserProfileBinding
import com.dizcoding.adapterdelegate.sample_one.SampleOneMenu
import com.dizcoding.adapterdelegate.sample_one.SampleOneMenus
import com.dizcoding.adapterdelegate.sample_one.SampleOneUserProfile

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val avatarUrl = "http://www.clker.com/cliparts/x/O/e/e/3/F/male-avatar-md.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DelegatesAdapter(
            sampleOneUserProfile {
                toast(it.userProfileUserName)
            },
            sampleOneMenus{
                toast(it.menuName)
            }
        )

        binding.mainRv.layoutManager = LinearLayoutManager(this)
        binding.mainRv.adapter = adapter

        adapter.submitList(
            listOf(
                SampleOneUserProfile(
                    avatarUrl,
                    "Gandhi Wibowo",
                    "Administrator",
                    "ADM-001"
                ),
                SampleOneMenus(
                    listOf(
                        SampleOneMenu(resources.getDrawable(R.drawable.vector_ic_calendar), "Calendar"),
                        SampleOneMenu(resources.getDrawable(R.drawable.vector_ic_claim), "Claim"),
                        SampleOneMenu(resources.getDrawable(R.drawable.vector_ic_loan), "Loan"),
                        SampleOneMenu(resources.getDrawable(R.drawable.vector_ic_payslip), "Payslip")
                    )
                )
            )
        )
    }

    private fun toast(message: String?) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

    private fun sampleOneUserProfile(
        itemClick: (SampleOneUserProfile) -> Unit
    ) = itemDelegate<SampleOneUserProfile>(R.layout.sample_one_user_profile)
        .click(itemClick)
        .bind {
            val radius =
                containerView.context.resources.getDimensionPixelSize(R.dimen.space_15)
            val binding = SampleOneUserProfileBinding.bind(containerView)

            Glide
                .with(containerView.context)
                .load(it.userProfileAvatarUrl)
                .transform(RoundedCorners(radius))
                .into(binding.ivUserProfile)

            binding.tvUserId.text = it.userProfileUserId
            binding.tvUserName.text = it.userProfileUserName
            binding.tvUserRole.text = it.userProfileUserRole
        }

    private fun sampleOneMenus(itemClick: (SampleOneMenu) -> Unit) =
        itemDelegate<SampleOneMenus>(R.layout.sample_one_menus)
            .bind {
                val adapter = DelegatesAdapter(
                    sampleOneMenu(itemClick)
                )
                val binding = SampleOneMenusBinding.bind(containerView)
                binding.rvMenu.apply {
                    layoutManager = GridLayoutManager(containerView.context, 4)
                    this.adapter = adapter
                }
                adapter.submitList(it.menus)
            }

    private fun sampleOneMenu(itemClick: (SampleOneMenu) -> Unit) =
        itemDelegate<SampleOneMenu>(R.layout.sample_one_menu)
            .click(itemClick)
            .bind {
                val binding = SampleOneMenuBinding.bind(containerView)
                binding.tvMenuName.text = it.menuName
                Glide
                    .with(containerView.context)
                    .load(it.menuImage)
                    .into(binding.ivMenuItem)
            }
}