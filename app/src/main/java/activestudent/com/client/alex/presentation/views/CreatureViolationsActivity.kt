package activestudent.com.client.alex.presentation.views

import activestudent.com.client.alex.App
import activestudent.com.client.alex.R
import activestudent.com.client.alex.model.Messages
import activestudent.com.client.alex.presentation.mvp.presenterImpl.CreatureViolationsPresenterImpl
import activestudent.com.client.alex.presentation.mvp.view.CreatureViolationsView
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_creature_violations.*
import kotlinx.android.synthetic.main.include_layout.*
import kotlinx.android.synthetic.main.include_layout.view.*
import kotlinx.android.synthetic.main.my_toolbar.*
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CreatureViolationsActivity : AppCompatActivity(), CreatureViolationsView {


    @Inject
    lateinit var messages: Messages
    @Inject
    lateinit var presenter: CreatureViolationsPresenterImpl
    private var progressDialog: ProgressDialog? = null

    override fun showProgressDialog() {
        progressDialog = indeterminateProgressDialog("Отправка сообщения")
        progressDialog!!.show()
    }

    override fun onFailure() {
        toast("Сообщение не отправлено.")
        startActivity(intentFor<AccountStudent>().clearTop())
    }

    override fun onSuccess() {
        toast("Сообщение успешно отправлено.")
        startActivity(intentFor<AccountStudent>().clearTop())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }

    override fun hideProgressDialog() {
        progressDialog!!.hide()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creature_violations)
        App.component.inject(this)
        presenter.attachView(this)
        setSupportActionBar(my_toolbar)
        setListener()
    }

    fun setListener() {
        btnSend.setOnClickListener({ onClickBtnSend() })
    }

    fun onClickBtnSend() {
        if (formCreature.numRoom.text.length == 0)
            toast("Заполните все поля!")
        else {
            messages.location = numRoom.text.toString()
            messages.time_state = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            messages.description = description.text.toString()
            messages.id_stud = presenter.getStudId()
            presenter.sendMessage()
        }
    }
}
