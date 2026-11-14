package mx.uv.luzioteamtime.controlador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest solicitud, HttpServletResponse respuesta, Object controlador) throws Exception {

        HttpSession sesion = solicitud.getSession(false);

        if (sesion == null || sesion.getAttribute("usuarioId") == null) {

            respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            respuesta.setContentType("application/json");
            respuesta.setCharacterEncoding("UTF-8");

            respuesta.getWriter().write(
                "{\"mensaje\":\"No hay una sesión activa\"}"
            );

            return false;
        }

        return true;
    }
}