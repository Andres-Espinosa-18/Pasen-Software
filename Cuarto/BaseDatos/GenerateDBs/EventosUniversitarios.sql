/*==============================================================*/
/* DBMS name:      ORACLE Version 19c                           */
/* Created on:     23/11/2025 15:12:03                          */
/*==============================================================*/


alter table COMENTARIO
   drop constraint FK_COMENTAR_COMENTARI_EVENTO;

alter table HORARIO
   drop constraint FK_HORARIO_CONTIENE_UBICACIO;

alter table HORARIO
   drop constraint FK_HORARIO_ES_ORGANI_EVENTO;

alter table INSCRIPCION
   drop constraint FK_INSCRIPC_SER_ASIST_EVENTO;

drop index COMENTARIO2_FK;

drop table COMENTARIO cascade constraints;

drop table EVENTO cascade constraints;

drop index CONTIENE_FK;

drop index ES_ORGANIZADO_FK;

drop table HORARIO cascade constraints;

drop index SER_ASISTIDO_FK;

drop table INSCRIPCION cascade constraints;

drop table UBICACION cascade constraints;

/*==============================================================*/
/* Table: COMENTARIO                                            */
/*==============================================================*/
create table COMENTARIO (
   EVE_ID               VARCHAR2(10)          not null,
   PER_ID               VARCHAR2(10)          not null,
   ASI_ID               CLOB                  not null,
   CALIFICACION         NUMBER(1)             not null
      constraint CKC_CALIFICACION_COMENTAR check (CALIFICACION in (1,2,3,4,5)),
   COMENTARIO           CLOB,
   constraint PK_COMENTARIO primary key (EVE_ID, PER_ID, ASI_ID)
);

/*==============================================================*/
/* Index: COMENTARIO2_FK                                        */
/*==============================================================*/
create index COMENTARIO2_FK on COMENTARIO (
   EVE_ID ASC
);

/*==============================================================*/
/* Table: EVENTO                                                */
/*==============================================================*/
create table EVENTO (
   EVE_ID               VARCHAR2(10)          not null,
   PER_ID               VARCHAR2(10),
   ORG_ID               VARCHAR2(10),
   EVE_NOMBRE           VARCHAR2(20)          not null,
   EVE_DESCRIPCION      CLOB                  not null,
   TIPO_EVENTO          VARCHAR2(30)          not null,
   constraint PK_EVENTO primary key (EVE_ID)
);

/*==============================================================*/
/* Table: HORARIO                                               */
/*==============================================================*/
create table HORARIO (
   EVE_ID               VARCHAR2(10)          not null,
   UBI_NOMBRE           VARCHAR2(20)          not null,
   FECHA                DATE,
   HORA_INICIO          DATE,
   HORA_FIN             DATE,
   DISPONIBILIDAD       SMALLINT,
   constraint PK_HORARIO primary key (EVE_ID, UBI_NOMBRE)
);

comment on column HORARIO.DISPONIBILIDAD is
'si est� disponible tornoara como True, caso contrario, si est� ocupada, se tornar� como False';

/*==============================================================*/
/* Index: ES_ORGANIZADO_FK                                      */
/*==============================================================*/
create index ES_ORGANIZADO_FK on HORARIO (
   EVE_ID ASC
);

/*==============================================================*/
/* Index: CONTIENE_FK                                           */
/*==============================================================*/
create index CONTIENE_FK on HORARIO (
   UBI_NOMBRE ASC
);

/*==============================================================*/
/* Table: INSCRIPCION                                           */
/*==============================================================*/
create table INSCRIPCION (
   EVE_ID               VARCHAR2(10)          not null,
   PER_ID               VARCHAR2(10)          not null,
   ASI_ID               CLOB                  not null,
   INSCRITO             SMALLINT              not null
      constraint CKC_INSCRITO_INSCRIPC check (INSCRITO in (S)),
   ESTADO_DE_INSCRIPCION SMALLINT              not null,
   FECHA_INSCRIPCION    DATE                  not null,
   constraint PK_INSCRIPCION primary key (EVE_ID, PER_ID, ASI_ID)
);

comment on column INSCRIPCION.INSCRITO is
'Si el asistente se inscribe a un evento, se define como True y no ser� capaz de inscribirse a otro evento, caso contrario, ser� false y podr� inscribirse a otro evento';

comment on column INSCRIPCION.ESTADO_DE_INSCRIPCION is
'Ser� True si el asistente confirma su llegada, caso contrario se tomara como False (Cancelada)';

/*==============================================================*/
/* Index: SER_ASISTIDO_FK                                       */
/*==============================================================*/
create index SER_ASISTIDO_FK on INSCRIPCION (
   EVE_ID ASC
);

/*==============================================================*/
/* Table: UBICACION                                             */
/*==============================================================*/
create table UBICACION (
   UBI_NOMBRE           VARCHAR2(20)          not null,
   UBI_CAPACIDAD        NUMBER(4)             not null,
   constraint PK_UBICACION primary key (UBI_NOMBRE)
);

alter table COMENTARIO
   add constraint FK_COMENTAR_COMENTARI_EVENTO foreign key (EVE_ID)
      references EVENTO (EVE_ID);

alter table HORARIO
   add constraint FK_HORARIO_CONTIENE_UBICACIO foreign key (UBI_NOMBRE)
      references UBICACION (UBI_NOMBRE);

alter table HORARIO
   add constraint FK_HORARIO_ES_ORGANI_EVENTO foreign key (EVE_ID)
      references EVENTO (EVE_ID);

alter table INSCRIPCION
   add constraint FK_INSCRIPC_SER_ASIST_EVENTO foreign key (EVE_ID)
      references EVENTO (EVE_ID);

