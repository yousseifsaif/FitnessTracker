import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class WorkoutExpandableListAdapter(
    private val context: Context,
    private val categories: List<String>,
    private val exercises: List<List<Any>>,  // ✅ تأكد من أنه List<List<String>>
    private val viewModel: WorkoutViewModel
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = categories.size

    override fun getChildrenCount(groupPosition: Int): Int = exercises[groupPosition].size

    override fun getGroup(groupPosition: Int): Any = categories[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return exercises[groupPosition][childPosition] // ✅ تأكد من إرجاع عنصر String وليس قائمة
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = (groupPosition * 100 + childPosition).toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getGroup(groupPosition) as String
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        val exerciseItem = getChild(groupPosition, childPosition) // نحصل على العنصر

        // تحويل العنصر إلى String إذا كان من النوع الصحيح
        textView.text = exerciseItem.toString()

        textView.setPadding(70, 10, 10, 10)

        view.setOnClickListener {
            viewModel.selectWorkout(exerciseItem.toString())  // تمرير النص إلى ViewModel
        }

        return view
    }
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}
