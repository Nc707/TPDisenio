package edu.inbugwethrust.premier.suite.controllers;

import edu.inbugwethrust.premier.suite.model.Usuario;
import edu.inbugwethrust.premier.suite.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }


    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
