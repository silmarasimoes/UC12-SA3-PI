package com.AppPI.AppPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.AppPI.AppPI.repository.MusicaRepository;
import com.AppPI.AppPI.repository.UsuarioRepository;

@Controller
public class BuscaController {
	
	@Autowired
	private UsuarioRepository ur;
	
	
	@Autowired
	private MusicaRepository mr;
	
	//GET
	@RequestMapping("/")
	public ModelAndView abrirIndex() {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	
	//POST
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView buscarIndex(@RequestParam("buscar") String buscar, @RequestParam("nome") String nome) {
		
		ModelAndView mv = new ModelAndView("index");
		String mensagem = "Resultados da busca por " + buscar;
		
		if(nome.equals("nomeusuario")) {
			mv.addObject("usuarios", ur.findByNomes(buscar));
			
		}else if(nome.equals("nomemusica")) {
			mv.addObject("musicas", mr.findByNomesMusicas(buscar));
			
		}else {
			mv.addObject("usuarios", ur.findByNomes(buscar));
			mv.addObject("musicas", mr.findByNomesMusicas(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;
	}

}
