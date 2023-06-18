function validarFormulario(event){

    event.preventDefault(); // Evita que el formulario se envíe automáticamente

    // Validación del campo "nombre"
    var nombre = document.getElementById('nombre').value;
    if (nombre.trim() === '') {
        alert('Por favor, introduce un nombre válido');
        return;
    }

    // Validación del campo "apellidos"
    var apellidos = document.getElementById('apellidos').value;
    if (apellidos.trim() === '') {
        alert('Por favor, introduce apellidos válidos');
        return;
    }

    var telefono = document.getElementById('telefono').value;
    // Expresión regular para verificar el formato de 9 dígitos
    var regex = /^[0-9]{9}$/;
  
    // Comprueba si el número de teléfono es válido
    if (!regex.test(telefono)) {
      alert('Por favor, introduce un número de teléfono válido de 9 dígitos');
      return;
    }

    // Validación del campo "email"
    var email = document.getElementById('email').value;
    var emailRegex = /^[^\s@]+@[^\s@]+$/;

    if (email.trim() === '' || !emailRegex.test(email)) {
        alert('Por favor, introduce un correo electrónico válido');
        return;
    }

    // Validación del campo "fechaNacimiento"
    var fechaNacimiento = document.getElementById('fechaNacimiento').value;
    if (fechaNacimiento.trim() === '') {
        alert('Por favor, introduce una fecha de nacimiento válida');
        return;
    }

    // Validación del campo "dni"
    var dni = document.getElementById('dni').value;
    var dniRegex = /^\d{8}[A-Z]$/;
    if (dni.trim() === '' || !dniRegex.test(dni)) {
        alert('Por favor, introduce un DNI válido');
        return;
    }

    // Validación del campo "imageForm"
    var imageForm = document.getElementById('imageForm');
    if (imageForm.files.length === 0) {
        alert('Por favor, selecciona una imagen');
        return;
    }

    // Si todos los campos son correctos, se enviará el formulario
    document.getElementById('formulario').submit();
}

function validarNombre(event){

    event.preventDefault(); // Evita que el formulario se envíe automáticamente

    // Validación del campo "nombre"
    var nombre = document.getElementById('nombre').value;
    if (nombre.trim() === '') {
        alert('Por favor, introduce un nombre válido');
        return;
    }

    document.getElementById('formulario').submit();
}