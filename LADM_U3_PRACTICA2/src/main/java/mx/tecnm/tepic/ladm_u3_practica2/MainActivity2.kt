package mx.tecnm.tepic.ladm_u3_practica2

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.button3
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var baseDatos =BaseDatos(this,"basedatos1",null,1)
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extra = intent.extras
        id = extra!!.getString("idactualizar")!!

        textView.setText(textView.text.toString()+ "${id}")
        try {
            var base = baseDatos.readableDatabase
            var respuesta = base.query("EVENTO", arrayOf("FECHA","HORA","LUGAR","DESCRIPCION"),"ID=?", arrayOf(id),null,null,null)


            if(respuesta.moveToFirst()){
                actualizartitulo.setText(respuesta.getString(0))
                actualizarhora.setText(respuesta.getString(1))
                actualizartitulo.setText(respuesta.getString(2))
                actualizarcontenido.setText(respuesta.getString(3))
            }else{
                mensaje("NO FUE POSIBLE ENCONTRAR ID")
            }
            base.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!)
        }

        button3.setOnClickListener {
            actualizar(id)

        }
        button4.setOnClickListener {
            finish()
        }
    }
    private fun actualizar(id: String){
        try {
            var  trans = baseDatos.writableDatabase
            var valores = ContentValues()

            valores.put("FECHA", actualizarfecha.text.toString())
            valores.put("HORA", actualizarhora.text.toString())
            valores.put("LUGAR", actualizartitulo.text.toString())
            valores.put("DESCRIPCION", actualizarcontenido.text.toString())

            var res = trans.update("EVENTO", valores, "ID=?", arrayOf(id))
            if (res>0){
                mensaje("Actualizado")
                finish()

                val intento1 = Intent(this, MainActivity::class.java)
                startActivity(intento1)

            }else {
                mensaje("No fue posible actualizar ID")
            }
            trans.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!)
        }
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){
                    d,i-> d.dismiss()
            }
            .show()

    }
}