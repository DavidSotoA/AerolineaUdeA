CREATE TABLE pasajero(
  id_pasajero int(3) AUTO_INCREMENT NOT NULL,
  token varchar(45) UNIQUE,
  username varchar (100) NOT NULL UNIQUE, 
  nombres varchar(100) NOT NULL,
  apellidos varchar(100) NOT NULL,
  fecha_nacimiento date NOT NULL,
  movil varchar(45) NOT NULL,
  email varchar(45) NOT NULL,
  tarjeta_de_credito varchar(45),
  numero_millas int DEFAULT 0,
  contraseña varchar(100),
  socio boolean DEFAULT false,
  PRIMARY KEY(id_pasajero)
);

create table ciudad(
    id int(3) auto_increment primary key,
    nombre varchar(50) not null
);

create table vuelo(
    id int(4) auto_increment not null,
    origen int(3) not null,
    destino int(3) not null,
    fecha date not null,
    duracion int(8),
    PRIMARY KEY (id),
    FOREIGN KEY (origen) REFERENCES ciudad(id),
    FOREIGN KEY (destino) REFERENCES ciudad(id)
);

create table asiento(
    id int (4) auto_increment not null,
    clase varchar(46) not null,
    precio int(9) not null,
    diponible boolean not null,
    precio_millas int(9),
    vuelo int(4) not null,
    primary key(id),
    foreign key(vuelo) references vuelo(id)
);


create table boleto(
    id int(4) auto_increment not null,
    check_in boolean not null,
    fecha_creacion datetime not null,
    fecha_vencimiento datetime not null,
    asiento int(4),
    pasajero int(4),
    primary key(id),
    foreign key (asiento) references asiento(id),
    foreign key (pasajero) references pasajero(id_pasajero)
); 


insert into ciudad(nombre) values('Medellin');
insert into ciudad(nombre) values('Cali');
insert into ciudad(nombre) values('Bogota');
insert into ciudad(nombre) values('Barcelona');
insert into ciudad(nombre) values('Paris');
insert into ciudad(nombre) values('Egipto');

insert into vuelo(origen,destino,fecha,duracion) values(1,2,now(), 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,2,'2010-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,2,'2011-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,2,'2012-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,2,'2013-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,2,'2014-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(4,2,'2011-11-27', 240);
insert into vuelo(origen,destino,fecha,duracion) values(1,3,'2012-11-27', 240);

insert into asiento(clase,precio,diponible,precio_millas,vuelo) values('VIP',50000,true,40000,7);
insert into asiento(clase,precio,diponible,precio_millas,vuelo) values('VIP',50000,true,40000,8);
insert into asiento(clase,precio,diponible,precio_millas,vuelo) values('NORMAL',30000,true,20000,8);
insert into asiento(clase,precio,diponible,precio_millas,vuelo) values('VIP',50000,true,40000,2);



