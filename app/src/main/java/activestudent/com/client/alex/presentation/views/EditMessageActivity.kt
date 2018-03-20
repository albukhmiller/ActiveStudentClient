package activestudent.com.client.alex.presentation.views

import activestudent.com.client.alex.App
import activestudent.com.client.alex.R
import activestudent.com.client.alex.presentation.mvp.presenterImpl.EditMessagePresenterImpl
import activestudent.com.client.alex.presentation.mvp.view.EditMessageView
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.include_layout.*
import kotlinx.android.synthetic.main.my_toolbar.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import javax.inject.Inject

class EditMessageActivity : AppCompatActivity(), EditMessageView {


    @Inject
    lateinit var editMsgPresenter: EditMessagePresenterImpl
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(my_toolbar)
        setContentView(R.layout.activity_edit)
        App.component.inject(this)
        setClickListener()
        editMsgPresenter.attachView(this)
        editMsgPresenter.setMessages(intent.getParcelableExtra("message"))
        description.setText(editMsgPresenter.onInitDescriptionField())
        numRoom.setText(editMsgPresenter.onInitLocationField())

    }

    override fun onSuccess() {
        toast("Сообщение успешно обновлено")
        finish()

    }

    override fun finish() {
        intent.putExtra("dataArray", arrayListOf(numRoom.text.toString(), description.text.toString()))
        setResult(RESULT_OK, intent)
        super.finish()
    }

    override fun getDescription() = description.text.toString()

    override fun getLocation() = numRoom.text.toString()

    override fun onFailure() {
        toast("Сообщение не обновлено")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        editMsgPresenter.detachView()
        if (progressDialog != null)
            progressDialog?.dismiss()
    }


    fun setClickListener() {
        btnSaveEdit.setOnClickListener({ onClickBtnSaveEdit() })
    }

    fun onClickBtnSaveEdit() {

        editMsgPresenter.onUpdateMessage()
    }

    override fun showProgressDialog() {
        progressDialog = indeterminateProgressDialog("Отправка сообщения")
        progressDialog!!.show()
    }

    override fun hideProgressDialog() {
        progressDialog!!.hide()
    }
}
