
//Estilos
document.addEventListener("DOMContentLoaded", function () {
    //Menu color rojo
    const linkmenug = document.querySelectorAll('.menugrande a');
    linkmenug.forEach((link)=>{
        link.addEventListener("mouseover",event =>{
            link.style.color = '#ec5242';
        })
        link.addEventListener("mouseout",event =>{
            link.style.color = 'rgba(0, 0, 0, 0.597)';
        })
        
    });

    //bloque borde blanco
    const bloque = document.querySelectorAll('div.contenedorprogramas>div');
    bloque.forEach(bloq=>{
        bloq.addEventListener("mouseover",event=>{
            bloq.style.border = '2px solid white';
        })
        bloq.addEventListener("mouseout",event=>{
            bloq.style.border = '0px';
        })
    });

    //Presentador Hacer grandela imagen y filtro gris
    const imagenes = document.querySelectorAll('div.presentador img')
    imagenes.forEach(img=>{
        img.addEventListener("mouseover",event=>{
            img.style.filter = 'grayscale(100%)';
            img.style.width = '28%';
            img.style.zIndex = '2';
        })
        img.addEventListener("mouseout",event=>{
            img.style.filter = 'grayscale(0%)';
            img.style.width = '25%';
            img.style.zIndex = '2';
        })
    })

    //Patrocinadores Hacer grande la imagen
    const patrocinadores = document.querySelectorAll('div.patrocinadores div img')
    patrocinadores.forEach(patro=>{
        patro.addEventListener("mouseover",event=>{
            patro.style.width = '17%';
            patro.style.height = '17%';
        })
        patro.addEventListener("mouseout",event=>{
            patro.style.width = '15%';
            patro.style.height = '15%';
        })
    })
});

// Contador

// Establecer la fecha límite
const fecha_limite = new Date("August 28, 2023 00:00:00").getTime();

// Actualizar el contador cada segundo
const contador = setInterval(function() {

  // Obtener la fecha y hora actual
  const ahora = new Date().getTime();

  // Calcular la diferencia entre la fecha límite y la fecha actual
  const restante = fecha_limite - ahora;

  // Calcular el tiempo restante en días, horas, minutos y segundos
  const dias = Math.floor(restante / (1000 * 60 * 60 * 24));
  const horas = Math.floor((restante % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutos = Math.floor((restante % (1000 * 60 * 60)) / (1000 * 60));
  const segundos = Math.floor((restante % (1000 * 60)) / 1000);

  // Mostrar el tiempo restante en el elemento de HTML correspondiente
  const pcontador = document.getElementById("contador");
  pcontador.innerHTML = `Quedan ${dias}días ${horas}horas ${minutos}minutos ${segundos}segundos`;

  // Si el tiempo restante es cero o menos, detener el contador
  if (restante <= 0) {
    clearInterval(contador);
  }

}, 1000);



  