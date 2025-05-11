import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutViewModel : ViewModel() {

    private val _selectedWorkout = MutableLiveData<Map<String, List<String>>>(emptyMap())
    val selectedWorkout: LiveData<Map<String, List<String>>> get() = _selectedWorkout

    private val _exerciseList = MutableLiveData<MutableList<String>>(mutableListOf())
    val exerciseList: MutableLiveData<MutableList<String>> get() = _exerciseList

    private val _workoutList = MutableLiveData<MutableMap<String, List<String>>>(mutableMapOf())
    val workoutList: MutableLiveData<MutableMap<String, List<String>>> get() = _workoutList

    fun addWorkout(category: String, exercises: List<Any>) {
        val currentMap = _workoutList.value?.toMutableMap() ?: mutableMapOf()
        currentMap[category] = exercises as List<String>

        _workoutList.value = currentMap
    }

    fun selectWorkout(workoutName: String) {
        val selectedExercises = _workoutList.value?.get(workoutName) ?: emptyList()
        _selectedWorkout.value =
            mapOf(workoutName to selectedExercises) // ✅ تأكدنا أن LiveData يتم تحديثه
    }


    fun addExercise(selectedExercise: String) {
        _exerciseList.value = _exerciseList.value?.apply {
            add(selectedExercise)
        }
    }
}