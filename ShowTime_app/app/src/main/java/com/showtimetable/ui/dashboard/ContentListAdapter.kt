package com.showtimetable.ui.dashboard

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.showtimetable.CustomToast
import com.showtimetable.R
import com.showtimetable.data.CalendarData
import com.showtimetable.sharedpreference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_table.*

class ContentListAdapter(
    var context: Context,
    var itemList:ArrayList<CalendarData.CalendarItem>,
    var listener:Listener,
    var y:Int,
    var m:Int,
    var d:Int
): RecyclerView.Adapter<ContentListAdapter.ViewHolder>() {

    interface Listener{
        fun onClick(pos:Int)
        fun refresh()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.calendar_schedule_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = itemList[position]
        holder.content.text = data.content
        val shape = GradientDrawable()
        shape.setColor(Color.parseColor(data.color))
        shape.shape = GradientDrawable.OVAL
        shape.setStroke(0, Color.parseColor(data.color))
        holder.circle.background = shape
        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
        holder.content.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage(holder.content.text.toString()).setTitle("일정")
            builder.setPositiveButton("일정 삭제"){
                    _,_->
                var pref = PreferenceManager(context)
                pref.deleteDaySchedule(y, m, d, position)
                val str = "${m}월 ${d}일 일정이 삭제되었습니다."
                CustomToast(context, str).show()
                listener.refresh()
            }
            builder.setNegativeButton("닫기"){
                    _,_->
            }

            val dlg = builder.create()
            dlg.show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView
        var circle:View

        init {
            content = itemView.findViewById(R.id.content)
            circle = itemView.findViewById(R.id.circle)
        }

    }

}
