package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

// Definición de la clase LoginFragment que extiende Fragment y
// implementa Response.Listener<JSONObject> y Response.ErrorListener
class LoginFragment : Fragment(), Response.Listener<JSONObject>, Response.ErrorListener {
    private lateinit var requestQueue: RequestQueue
    private lateinit var nameUser: EditText
    private lateinit var passUser: EditText
    private lateinit var btnLogin: Button

    // Método llamado cuando se crea la vista del fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de la cola de solicitudes de Volley
        requestQueue = Volley.newRequestQueue(context)

        // Inicialización de los elementos de la interfaz de usuario
        nameUser = view.findViewById(R.id.nameUser)
        passUser = view.findViewById(R.id.passUser)
        btnLogin = view.findViewById(R.id.btnLogin)

        // Configuración del evento de clic en el botón de inicio de sesión
        btnLogin.setOnClickListener {
            iniciarSesion()
        }
    }

    // Método llamado para crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    // Método para iniciar sesión
    fun iniciarSesion() {
        val url =
            "https://crematory-helmets.000webhostapp.com/Session.php?user=${nameUser.text.toString()}&pwd=${passUser.text.toString()}"

        // Realizar una solicitud GET con Volley
        try {
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    // Manejar la respuesta exitosa
                    if (response != null || response != "[]") {
                        onResponse(JSONObject(response))
                    } else {
                        Response.ErrorListener { "No se ha encontrado el usuario" }
                    }
                },
                Response.ErrorListener { error ->
                    // Manejar errores de Volley
                    if (error == null)
                        Toast.makeText(context, "Error, no se ha encontrado al usuario", Toast.LENGTH_SHORT).show()
                    else
                        if (error is VolleyError)
                            onErrorResponse(error)
                })
            requestQueue.add(stringRequest)

        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    // Método llamado cuando se recibe una respuesta exitosa de la solicitud
    override fun onResponse(response: JSONObject) {
        val usuario = User()

        val jsObject: JSONObject? = response.optJSONArray("datos")?.getJSONObject(0)
        if (jsObject != null) {
            usuario.password = jsObject.optString("Username")
            usuario.nombre = jsObject.optString("Name")
            usuario.password = jsObject.optString("Password")

            if (usuario.password == "0" && usuario.nombre == "0" && usuario.password == "0") {
                Toast.makeText(context, "No se ha encontrado al usuario!", Toast.LENGTH_SHORT).show()
            } else {
                // Iniciar una actividad con la información del usuario
                val intent = Intent(context, MainActivity2::class.java)
                intent.putExtra("nombre", usuario.nombre)
                startActivity(intent)
                Toast.makeText(context, "Conectado correctamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método llamado cuando se produce un error en la solicitud de Volley
    override fun onErrorResponse(error: VolleyError) {
        if (error.networkResponse != null) {
            Toast.makeText(
                context,
                "Error en la solicitud: ${error.message} con indicio de error: ${error.networkResponse.statusCode}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "Error sin respuesta de red: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
