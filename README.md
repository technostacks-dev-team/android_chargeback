# Project Code Index

This document provides a file-wise overview of the Kotlin/Android codebase with brief details of functions and responsibilities.

## Module: `app`

### Data models (`app/src/main/java/com/technostacks/chargebackui/data`)

- `Category.kt`
  - Type: `data class Category(id: String, name: String, iconRes: Int)`
  - Purpose: Represents a subscription category with a name and icon resource.

- `Frequency.kt`
  - Type: `data class Frequency(id: String, name: String)`
  - Purpose: Represents billing frequency (e.g., Weekly/Monthly/Annually).

- `Service.kt`
  - Type: `data class Service(id: String, name: String, iconRes: Int, defaultAmount: Double)`
  - Purpose: Represents a service (e.g., Netflix) with default amount and icon.

- `Subscription.kt`
  - Type: `data class Subscription(service: Service?, amount: Double, category: Category?, startDate: String, frequency: Frequency?, isActive: Boolean)`
  - Purpose: Aggregates user selections for a subscription being created/edited.

### Adapters (`app/src/main/java/com/technostacks/chargebackui/mvvm/adapters`)

- `CategoryAdapter.kt`
  - Type: `RecyclerView.Adapter`
  - State: `categories: List<Category>`, `selectedCategory: Category?`
  - Functions:
    - `submitList(list: List<Category>)`: Updates list and refreshes UI.
    - `onCreateViewHolder(parent, viewType)`: Inflates `ItemCategoryBinding` and returns `ViewHolder`.
    - `onBindViewHolder(holder, position)`: Binds item at position.
    - `getItemCount()`: Returns list size.
    - `ViewHolder.bind(category: Category)`: Binds name/icon, toggles check icon if selected, handles item click to update selection and invoke callback.

- `FrequencyAdapter.kt`
  - Type: `RecyclerView.Adapter`
  - State: `frequencies: List<Frequency>`, `selectedFrequency: Frequency?`
  - Functions:
    - `submitList(list: List<Frequency>)`: Updates list and refreshes UI.
    - `onCreateViewHolder(parent, viewType)`: Inflates `ItemFrequencyBinding` and returns `ViewHolder`.
    - `onBindViewHolder(holder, position)`: Binds item at position.
    - `getItemCount()`: Returns list size.
    - `ViewHolder.bind(frequency: Frequency)`: Binds name, shows check icon if selected by id, handles item click to update selection and notify only changed rows (previous and current), then invokes callback.

- `ServicesAdapter.kt`
  - Type: `RecyclerView.Adapter`
  - State: `services: List<Service>`, `selectedService: Service?`
  - Functions:
    - `submitList(list: List<Service>)`: Updates list and refreshes UI.
    - `onCreateViewHolder(parent, viewType)`: Inflates `ItemServiceBinding` and returns `ViewHolder`.
    - `onBindViewHolder(holder, position)`: Binds item at position.
    - `getItemCount()`: Returns list size.
    - `ViewHolder.bind(service: Service)`: Binds icon/name, toggles check icon if selected, handles click to toggle selection, refreshes, then invokes callback.

### Extensions (`app/src/main/java/com/technostacks/chargebackui/mvvm/extensions`)

- `getQueryTextSubmittedStateFlow.kt`
  - `fun SearchView.getQueryTextSubmittedListener(onQueryTextSubmitted: (String?) -> Unit): SearchView.OnQueryTextListener`
    - Purpose: Attaches a `SearchView.OnQueryTextListener` that forwards both submit and text change events to the provided callback and returns the listener.
    - Inner overrides:
      - `onQueryTextSubmit(query: String?)`: Forwards query and consumes event.
      - `onQueryTextChange(newText: String?)`: Forwards text and consumes event.

### ViewModel (`app/src/main/java/com/technostacks/chargebackui/mvvm/viewmodel`)

- `CreateSubscriptionViewModel.kt`
  - Type: `ViewModel`
  - LiveData state:
    - `subscription: LiveData<Subscription>`
    - `services: LiveData<List<Service>>`
    - `categories: LiveData<List<Category>>`
    - `frequencies: LiveData<List<Frequency>>`
  - init block:
    - Loads services, categories, and frequencies.
    - Initializes `subscription` with default `startDate`, `frequency`, and `category`.
  - Functions:
    - `private fun loadServices()`: Seeds `services` list with static items.
    - `private fun loadCategories()`: Seeds `categories` list with static items.
    - `private fun loadFrequencies()`: Seeds `frequencies` list with static items.
    - `fun selectService(service: Service)`: Updates `subscription` with selected service and default amount.
    - `fun selectCategory(category: Category)`: Updates `subscription` category.
    - `fun selectFrequency(frequency: Frequency)`: Updates `subscription` frequency.
    - `fun setStartDate(date: String)`: Updates `subscription` start date.
    - `fun setAmount(amount: Double)`: Updates `subscription` amount.
    - `fun toggleActive(isActive: Boolean)`: Updates `subscription` active flag.
    - `fun searchServices(query: String): List<Service>`: Case-insensitive filter over current services.

### Views (`app/src/main/java/com/technostacks/chargebackui/mvvm/views`)

- `CreateSubscriptionActivity.kt`
  - Type: `AppCompatActivity`
  - Members: `viewModel: CreateSubscriptionViewModel by viewModels()`, `binding: ActivityCreateSubscriptionBinding`
  - Functions:
    - `onCreate(savedInstanceState: Bundle?)`: Sets up edge-to-edge, binding, insets, observers, and click listeners.
    - `private fun setInsets()`: Applies system bar insets as padding.
    - `private fun setupObservers()`: Observes `subscription` and calls `updateUI`.
    - `private fun updateUI(subscription: Subscription)`: Reflects current subscription state into the UI (icons, text, colors, enabled states).
    - `private fun setupClickListeners()`: Wires up toolbar back, save, and selection handlers for service, category, date, frequency, and active toggle.
    - `@OptIn(FlowPreview::class) private fun showServicesBottomSheet()`: Presents services bottom sheet with search; updates selection via adapter and confirms on Done.
    - `private fun showCategoryBottomSheet()`: Presents category selection bottom sheet and confirms on Done.
    - `private fun showFrequencyBottomSheet()`: Presents frequency selection bottom sheet and confirms on Done.
    - `private fun showDatePicker()`: Shows date picker and sets formatted date into `ViewModel`.