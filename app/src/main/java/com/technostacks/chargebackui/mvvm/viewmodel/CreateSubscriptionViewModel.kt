package com.technostacks.chargebackui.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.technostacks.chargebackui.R
import com.technostacks.chargebackui.data.Category
import com.technostacks.chargebackui.data.Frequency
import com.technostacks.chargebackui.data.Service
import com.technostacks.chargebackui.data.Subscription

class CreateSubscriptionViewModel : ViewModel() {

    private val _subscription = MutableLiveData(Subscription())
    val subscription: LiveData<Subscription> = _subscription

    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> = _services

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _frequencies = MutableLiveData<List<Frequency>>()
    val frequencies: LiveData<List<Frequency>> = _frequencies

    init {
        loadServices()
        loadCategories()
        loadFrequencies()

        _subscription.value = Subscription(
            startDate = "Apr 12, 2025",
            frequency = Frequency("weekly", "Weekly"),
            category = Category("subscription", "Subscription", R.drawable.ic_subscription)
        )
    }

    private fun loadServices() {
        _services.value = listOf(
            Service("netflix", "Netflix", R.drawable.ic_netflix, 15.0),
            Service("hulu", "Hulu", R.drawable.ic_hulu, 12.99),
            Service("spotify", "Spotify", R.drawable.ic_spotify, 9.99),
            Service("playstation", "PlayStation+", R.drawable.ic_youtubbbe, 9.99),
            Service("paramount", "Paramount+", R.drawable.ic_paramount, 9.99),
            Service("youtube", "YouTube Music", R.drawable.ic_youtubbbe, 10.99)
        )
    }

    private fun loadCategories() {
        _categories.value = listOf(
            Category("subscription", "Subscription", R.drawable.ic_subscription_category),
            Category("utility", "Utility", R.drawable.ic_utility),
            Category("card_payment", "Card Payment", R.drawable.ic_card),
            Category("loan", "Loan", R.drawable.ic_loan),
            Category("rent", "Rent", R.drawable.ic_rent)
        )
    }

    private fun loadFrequencies() {
        _frequencies.value = listOf(
            Frequency("weekly", "Weekly"), Frequency("monthly", "Monthly"), Frequency("annually", "Annually")
        )
    }

    fun selectService(service: Service) {
        _subscription.value = _subscription.value?.copy(
            service = service, amount = service.defaultAmount
        )
    }

    fun selectCategory(category: Category) {
        _subscription.value = _subscription.value?.copy(category = category)
    }

    fun selectFrequency(frequency: Frequency) {
        _subscription.value = _subscription.value?.copy(frequency = frequency)
    }

    fun setStartDate(date: String) {
        _subscription.value = _subscription.value?.copy(startDate = date)
    }

    fun setAmount(amount: Double) {
        _subscription.value = _subscription.value?.copy(amount = amount)
    }

    fun toggleActive(isActive: Boolean) {
        _subscription.value = _subscription.value?.copy(isActive = isActive)
    }

    fun searchServices(query: String): List<Service> {
        return _services.value?.filter {
            it.name.contains(query, ignoreCase = true)
        } ?: emptyList()
    }
}
