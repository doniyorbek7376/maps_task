package uz.doniyorbekurinboyev.avitotech

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class FilterActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, FilterActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = ServiceAdapter()
        title = "Filter"
    }

    inner class ServiceHolder(var view: View): RecyclerView.ViewHolder(view){
        private var mSwitch: Switch = view.findViewById(R.id.switch_filter)
        private var mTextView : TextView = view.findViewById(R.id.service_text_view)
        private var mServiceName: String = "a"
        init{
            mSwitch.setOnCheckedChangeListener { _, isChecked ->
                Filter.filter[mServiceName] = isChecked
            }
        }
        fun bind(service: String){
            mSwitch.isChecked = Filter.filter[service] ?: false
            mServiceName = service
            mTextView.text = mServiceName
        }
    }
    inner class ServiceAdapter: RecyclerView.Adapter<ServiceHolder>(){
        private var mServices = Filter.filter.keys.toList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHolder {
            val view = LayoutInflater.from(this@FilterActivity).inflate(R.layout.list_filter, parent, false)
            return ServiceHolder(view)
        }

        override fun getItemCount(): Int {
            return mServices.size
        }

        override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
            holder.bind(mServices[position])
        }

    }
}