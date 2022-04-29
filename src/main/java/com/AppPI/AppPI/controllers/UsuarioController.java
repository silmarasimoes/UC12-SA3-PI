package com.AppPI.AppPI.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.AppPI.AppPI.models.Musica;
import com.AppPI.AppPI.models.Usuario;
import com.AppPI.AppPI.repository.MusicaRepository;
import com.AppPI.AppPI.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository ur;

	@Autowired
	private MusicaRepository mr;

	// GET que chama o form para cadastrar usuários
	@RequestMapping("/cadastrarUsuario")
	public String form() {
		return "usuario/form-usuario";
	}

	// POST que cadastra usuários
	@RequestMapping(value = "/cadastrarUsuario", method = RequestMethod.POST)
	public String form(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/cadastrarUsuario";
		}

		ur.save(usuario);
		attributes.addFlashAttribute("mensagem", "usuário cadastrado com sucesso!");
		return "redirect:/cadastrarUsuario";
	}

	// GET que lista usuários
	@RequestMapping("/usuarios")
	public ModelAndView listaUsuarios() {
		ModelAndView mv = new ModelAndView("usuario/lista-usuario");
		Iterable<Usuario> usuarios = ur.findAll();
		mv.addObject("usuarios", usuarios);
		return mv;
	}

	// GET que lista musicas e detalhes dos usuário
	@RequestMapping("/detalhes-usuario/{id}")
	public ModelAndView detalhesUsuario(@PathVariable("id") long id) {
		Usuario usuario = ur.findById(id);
		ModelAndView mv = new ModelAndView("usuario/detalhes-usuario");
		mv.addObject("usuarios", usuario);

		// lista de musicas baseada no id do usuário
		Iterable<Musica> musicas = mr.findByUsuario(usuario);
		mv.addObject("musicas", musicas);

		return mv;

	}

	// POST que adiciona musicas
	@RequestMapping(value="/detalhes-usuario/{id}", method = RequestMethod.POST)
	public String detalhesUsuarioPost(@PathVariable("id") long id, Musica musicas, BindingResult result,
			RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/detalhes-usuario/{id}";
		}
		
		Usuario usuario = ur.findById(id);
		musicas.setUsuario(usuario);
		mr.save(musicas);
		
		attributes.addFlashAttribute("mensagem", "Musica adicionado com sucesso");
		return "redirect:/detalhes-usuario/{id}";
		
	}
	
	//GET que deleta usuário
	@RequestMapping("/deletarUsuario")
	public String deletarUsuario(long id) {
		Usuario usuario = ur.findById(id);
		ur.delete(usuario);
		return "redirect:/usuarios";
		
	}
	
	// Métodos que atualizam usuário
	// GET que chama o FORM de edição do usuário
	@RequestMapping("/editar-usuario")
	public ModelAndView editarUsuario(long id) {
		Usuario usuario = ur.findById(id);
		ModelAndView mv = new ModelAndView("usuario/update-usuario");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	// POST que atualiza o usuário
	@RequestMapping(value = "/editar-usuario", method = RequestMethod.POST)
	public String updateUsuario(@Valid Usuario usuario,  BindingResult result, RedirectAttributes attributes){
		
		ur.save(usuario);
		attributes.addFlashAttribute("success", "usuário alterado com sucesso!");
		
		long idLong = usuario.getId();
		String id = "" + idLong;
		return "redirect:/detalhes-usuario/" + id;
		
	}
	
	// GET que deleta musica
	@RequestMapping("/deletarMusica")
	public String deletarMusica(long id) {
		
		Musica musica = mr.findById(id);
		
		Usuario usuario = musica.getUsuario();
		String codigo = "" + usuario.getId();
		
		mr.delete(musica);
		
		return "redirect:/detalhes-usuario/" + codigo;
	
	}
	
	@GetMapping("/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        Iterable<Usuario> listUsers = ur.findAll();
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Código", "Nome", "Login", "E-mail", "Senha", "CPF", "Data de nascimento", "Endereço"};
        String[] nameMapping = {"id", "nome", "login", "email", "senha","cpf","dataNascimento","endereco"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (Usuario user : listUsers) {
            csvWriter.write(user, nameMapping);
        }
         
        csvWriter.close();
         
    }
	
	@GetMapping("/exportMusicas")
    public void exportToCSVMusicas(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=musics_users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        Iterable<Musica> listUsers = mr.findAll();
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Código", "Nome", "Álbum", "Artista", "Estilo", "Link", "Usuário"};
        String[] nameMapping = {"id", "nome", "album", "artista", "estilo","linkMusica", "usuario"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (Musica user : listUsers) {
            csvWriter.write(user, nameMapping);
        }
         
        csvWriter.close();
         
    }
}
