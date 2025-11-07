package com.technostacks.chargebackui.mvvm.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.technostacks.chargebackui.R
import com.technostacks.chargebackui.data.Category
import com.technostacks.chargebackui.data.Frequency
import com.technostacks.chargebackui.data.Service
import com.technostacks.chargebackui.data.Subscription
import com.technostacks.chargebackui.databinding.ActivityCreateSubscriptionBinding
import com.technostacks.chargebackui.mvvm.adapters.CategoryAdapter
import com.technostacks.chargebackui.mvvm.adapters.FrequencyAdapter
import com.technostacks.chargebackui.mvvm.adapters.ServicesAdapter
import com.technostacks.chargebackui.mvvm.extensions.getQueryTextSubmittedListener
import com.technostacks.chargebackui.mvvm.viewmodel.CreateSubscriptionViewModel
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.FlowPreview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("DefaultLocale, SetTextI18n")
class CreateSubscriptionActivity : AppCompatActivity() {

    private val viewModel: CreateSubscriptionViewModel by viewModels()
    private lateinit var binding: ActivityCreateSubscriptionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSubscriptionBinding.inflate(layoutInflater)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        setContentView(binding.root)
        setInsets()

        setupObservers()
        setupClickListeners()
    }

    private fun setInsets() {
        binding.root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

    }

    private fun setupObservers() {
        viewModel.subscription.observe(this) { subscription ->
            updateUI(subscription)
        }
    }

    private fun updateUI(subscription: Subscription) {
        with(binding) {
            subscription.service?.let { service ->
                ivAdd.setImageResource(service.iconRes)
                ivAdd.setContentPadding(0,0,0,0)
                tvChooseService.text = service.name
                tvServiceName.text = service.name
                tvChooseService.setTextColor(getColor(R.color.black))
                tvServiceName.setTextColor(getColor(R.color.black))
                tvAmount.text = "$${String.format("%.2f", service.defaultAmount)}"
                tvAmountDesc.text = "$${String.format("%.2f", service.defaultAmount)}"
            } ?: run {
                ivAdd.setImageResource(R.drawable.ic_add)
                ivAdd.setContentPadding(15,15,15,15)
                tvChooseService.text = getString(R.string.choose_a_service)
                tvChooseService.setTextColor(getColor(R.color.default_grey))
                tvAmount.text = getString(R.string.temp_0_dollar)
                tvAmountDesc.text = getString(R.string.temp_0_dollar)
            }


            subscription.category?.let { category ->
                tvCategory.text = category.name
                tvCategory.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    category.iconRes, 0, R.drawable.ic_choose, 0
                )
            }

            tvStartDateValue.text = subscription.startDate

            subscription.frequency?.let { frequency ->
                tvFrequencyValue.text = frequency.name
            }

            switchActive.isChecked = subscription.isActive

            tvSave.isEnabled = subscription.service != null
            tvSave.setTextColor(
                if (subscription.service != null) getColor(R.color.btn_enabled)
                else getColor(R.color.default_grey)
            )
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }

            tvSave.setOnClickListener {
                viewModel.subscription.value?.let { subscription ->
                    if (subscription.service != null) {
                        // Handle save - pass data back or save to database
                        finish()
                    }
                }
            }

            clService.setOnClickListener {
                showServicesBottomSheet()
            }

            tvServiceName.setOnClickListener {
                showServicesBottomSheet()
            }

            tvCategory.setOnClickListener {
                showCategoryBottomSheet()
            }

            tvStartDateValue.setOnClickListener {
                showDatePicker()
            }

            tvFrequencyValue.setOnClickListener {
                showFrequencyBottomSheet()
            }

            switchActive.setOnCheckedChangeListener { _, isChecked ->
                viewModel.toggleActive(isChecked)
            }
        }
    }

    @OptIn(FlowPreview::class) private fun showServicesBottomSheet() {
        var selectedService: Service? = null
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_services, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvService)
        val searchField = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.svServices)
        val doneButton = view.findViewById<MaterialTextView>(R.id.doneButton)

        val adapter = ServicesAdapter(
            onServiceClick = {
                selectedService = it
            })

        doneButton.setOnClickListener {
            selectedService?.let {
                viewModel.selectService(selectedService)
                dialog.dismiss()
            } ?: dialog.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.services.value?.let { services ->
            adapter.submitList(services)
        }

        searchField.getQueryTextSubmittedListener { searchQuery ->
            val filtered = viewModel.searchServices(searchQuery.toString())
            adapter.submitList(filtered)
        }

        dialog.setContentView(view)
        dialog.show()
    }


    private fun showCategoryBottomSheet() {
        var selectedCategory: Category? = null
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_category, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCategory)
        val doneButton = view.findViewById<MaterialTextView>(R.id.doneButton)

        val adapter = CategoryAdapter(onCategoryClick = {
            selectedCategory = it
        })

        doneButton.setOnClickListener {
            selectedCategory?.let {
                viewModel.selectCategory(selectedCategory)
                dialog.dismiss()
            } ?: dialog.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.categories.value?.let { categories ->
            adapter.submitList(categories)
        }


        dialog.setContentView(view)
        dialog.show()
    }

    private fun showFrequencyBottomSheet() {
        var selectedFrequency: Frequency? = null

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_frequency, null)

        val rvFrequency = view.findViewById<RecyclerView>(R.id.rvFrequency)
        val doneBtn = view.findViewById<MaterialTextView>(R.id.doneButton)


        val adapter = FrequencyAdapter(onFrequencyClick = {
            selectedFrequency = it
        })

        doneBtn.setOnClickListener {
            selectedFrequency?.let {
                viewModel.selectFrequency(selectedFrequency)
                dialog.dismiss()
            } ?: dialog.dismiss()
        }

        rvFrequency.layoutManager = LinearLayoutManager(this)
        rvFrequency.adapter = adapter

        viewModel.frequencies.value?.let { frequencies ->
            adapter.submitList(frequencies)
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this, { _, year, month, day ->
                val date = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                viewModel.setStartDate(format.format(date.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
