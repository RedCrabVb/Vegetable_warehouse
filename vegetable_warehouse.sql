--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 14.1

-- Started on 2022-03-19 21:29:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 238 (class 1255 OID 25395)
-- Name: registration_client(character varying, character varying); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.registration_client(IN login_in character varying, IN password_in character varying)
    LANGUAGE plpgsql
    AS $$declare
begin
  with user_id_req as (
      insert into "User" ("login", "password") values (login_in, password_in) returning "id_user"
  ) insert into "Client" ("idUser", amount) values (
      (select "id_user" from "user_id_req"),
      1000
  );
end;
$$;


ALTER PROCEDURE public.registration_client(IN login_in character varying, IN password_in character varying) OWNER TO postgres;

--
-- TOC entry 237 (class 1255 OID 25396)
-- Name: registration_employee(character varying, character varying, character varying, character varying, character varying); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.registration_employee(IN full_name_in character varying, IN passport_in character varying, IN position_in character varying, IN login_in character varying, IN "password_In" character varying)
    LANGUAGE plpgsql
    AS $$declare
begin
  with user_id_req as (
      insert into "User" ("login", "password") values (login_in, "password_In") returning "id_user"
  ) insert into "Employee" ("idUser", full_name, passport, "idPosition") values (
      (select "id_user" from "user_id_req"),
      full_name_in,
      passport_in,
      (select "idPosition" from "Position" where "namePosition" = position_in)
  );
end;
$$;


ALTER PROCEDURE public.registration_employee(IN full_name_in character varying, IN passport_in character varying, IN position_in character varying, IN login_in character varying, IN "password_In" character varying) OWNER TO postgres;

--
-- TOC entry 239 (class 1255 OID 17169)
-- Name: sell_goods(bigint, bigint, bigint, date, bigint[]); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sell_goods(IN "idClinetIN" bigint, IN "idSellerIN" bigint, IN "sumIN" bigint, IN "datePay" date, IN "idGoodsIN" bigint[])
    LANGUAGE plpgsql
    AS $$
declare
    idPaymentsVAR integer := 0;
    amountVAR bigint := 0;
begin
 select "amount"  into amountVAR from "Client" where "idClient" = "idClinetIN";
 update "Client" set amount = (CASE
                                    WHEN amountVAR > "sumIN" THEN amountVAR - "sumIN"
                                    WHEN amountVAR <= "sumIN" THEN amountVAR
                                    END) where "idClient" = "idClinetIN";

 with idPaymentsIN as (
 	insert into public."Payments" ("paymentDate", "orderCompletionMark", "paymentAmount") values ("datePay", (CASE
                                    WHEN amountVAR > "sumIN" THEN 1
                                    WHEN amountVAR <= "sumIN" THEN 0
                                    END), "sumIN") returning *
 )
 (select "idPayments" into idPaymentsVAR from idPaymentsIN);

 for i in 1 .. array_upper("idGoodsIN", 1) loop
	raise notice 'counter: %', "idGoodsIN"[i];
    insert into "Sales" ("idSaller", "idClient", "idPayments", "idGoods") values ("idSellerIN", "idClinetIN", idPaymentsVAR, "idGoodsIN"[i]);
 end loop;

end; $$;


ALTER PROCEDURE public.sell_goods(IN "idClinetIN" bigint, IN "idSellerIN" bigint, IN "sumIN" bigint, IN "datePay" date, IN "idGoodsIN" bigint[]) OWNER TO postgres;

--
-- TOC entry 236 (class 1255 OID 17163)
-- Name: sell_goods(bigint, bigint, bigint, date, bigint); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sell_goods(IN "idClinetIN" bigint, IN "idSellerIN" bigint, IN "sumIN" bigint, IN "datePay" date, IN "idGoodsIN" bigint)
    LANGUAGE plpgsql
    AS $$
declare
-- variable declaration
begin
    call sell_goods("idClinetIN", "idSellerIN", "sumIN", "datePay", array["idGoodsIN"]);
end; $$;


ALTER PROCEDURE public.sell_goods(IN "idClinetIN" bigint, IN "idSellerIN" bigint, IN "sumIN" bigint, IN "datePay" date, IN "idGoodsIN" bigint) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 213 (class 1259 OID 17059)
-- Name: Client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Client" (
    "idClient" bigint NOT NULL,
    "idUser" bigint NOT NULL,
    amount bigint
);


ALTER TABLE public."Client" OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17107)
-- Name: Client_idClient_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Client" ALTER COLUMN "idClient" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Client_idClient_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 212 (class 1259 OID 17052)
-- Name: Employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Employee" (
    "idEmployee" bigint NOT NULL,
    full_name character varying(255) NOT NULL,
    passport text,
    "idPosition" bigint NOT NULL,
    "idUser" bigint NOT NULL
);


ALTER TABLE public."Employee" OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17110)
-- Name: Employee_idEmployee_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Employee" ALTER COLUMN "idEmployee" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Employee_idEmployee_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 209 (class 1259 OID 17031)
-- Name: Goods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Goods" (
    "idGoods" bigint NOT NULL,
    "nameGoods" character varying(255) NOT NULL,
    characteristics text NOT NULL,
    note text
);


ALTER TABLE public."Goods" OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17109)
-- Name: Goods_idGoods_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Goods" ALTER COLUMN "idGoods" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Goods_idGoods_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 214 (class 1259 OID 17064)
-- Name: Payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Payments" (
    "idPayments" bigint NOT NULL,
    "paymentDate" date,
    "orderCompletionMark" smallint,
    "paymentAmount" bigint
);


ALTER TABLE public."Payments" OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17117)
-- Name: Payments_idPayments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Payments" ALTER COLUMN "idPayments" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Payments_idPayments_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 210 (class 1259 OID 17038)
-- Name: Position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Position" (
    "idPosition" bigint NOT NULL,
    "namePosition" character varying(255) NOT NULL,
    salary bigint,
    note text
);


ALTER TABLE public."Position" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17111)
-- Name: Position_idPosition_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Position" ALTER COLUMN "idPosition" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Position_idPosition_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 215 (class 1259 OID 17069)
-- Name: Sales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Sales" (
    "idSaller" bigint NOT NULL,
    "idClient" bigint NOT NULL,
    "idPayments" bigint NOT NULL,
    "idGoods" bigint NOT NULL
);


ALTER TABLE public."Sales" OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 17045)
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."User" (
    id_user bigint NOT NULL,
    login character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public."User" OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17108)
-- Name: User_idUser_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."User" ALTER COLUMN id_user ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."User_idUser_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 17173)
-- Name: clientinfo; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.clientinfo AS
 SELECT "Client"."idClient",
    usr.login,
    "Client".amount
   FROM (public."Client"
     JOIN public."User" usr ON ((usr.id_user = "Client"."idUser")));


ALTER TABLE public.clientinfo OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 25360)
-- Name: employeeinfo; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.employeeinfo AS
 SELECT "Employee"."idEmployee",
    "Employee".full_name,
    usr.login,
    "Employee".passport,
    pos."namePosition",
    pos.salary
   FROM ((public."Employee"
     JOIN public."Position" pos ON ((pos."idPosition" = "Employee"."idPosition")))
     JOIN public."User" usr ON ((usr.id_user = "Employee"."idUser")));


ALTER TABLE public.employeeinfo OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 25355)
-- Name: salesinfo; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.salesinfo AS
 SELECT sales.full_name AS name_sales,
    clinet.login AS login_user,
    payments."paymentDate" AS payment_date,
    payments."orderCompletionMark" AS order_completion_mark,
    payments."paymentAmount" AS amount,
    goods."nameGoods" AS goods_name
   FROM ((((public."Sales"
     JOIN public."Employee" sales ON ((sales."idEmployee" = "Sales"."idSaller")))
     JOIN public.clientinfo clinet ON ((clinet."idClient" = "Sales"."idClient")))
     JOIN public."Payments" payments ON ((payments."idPayments" = "Sales"."idPayments")))
     JOIN public."Goods" goods ON ((goods."idGoods" = "Sales"."idGoods")));


ALTER TABLE public.salesinfo OWNER TO postgres;


--
-- TOC entry 3378 (class 0 OID 17052)
-- Dependencies: 212
-- Data for Name: Employee; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- TOC entry 3375 (class 0 OID 17031)
-- Dependencies: 209
-- Data for Name: Goods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Goods" ("idGoods", "nameGoods", characteristics, note) FROM stdin;
1	пшеница	Если купить яровые пшеничные семена, то их посев необходимо выполнить весной. Этот вид злака чувствителен к сорнякам, но способен выдерживать кратковременные заморозки и засуху. Причем мягкие сорта более устойчивы к холоду, чем твердые. Культура может выращиваться почти во всех агроклиматических зонах России, поскольку легко адаптируется к окружающим условиям	\N
2	кукуруза	Зубовидная с крупными зернами. Широко распространена в России, используется для производства муки, круп, спирта, крахмала, силоса. Злак содержит до 75 % крахмала, до 10 % белка и до 5 % растительного жира.	\N
3	подсолнух	Листья — крупные, мясистые, на длинных черешках, с пушком по поверхности. У масличных сортов вырастает до 30 листьев. Соцветие — круглая корзинка с ярко-желтыми лепестками, диаметр от 10 до 40 и более см. Плод — продолговатая семянка с четырьмя гранями, снаружи кожура, внутри семя.	\N
4	картошка	Многолетнее травянистое клубненосное растение, возделываемое как однолетняя культура. Представляет собой куст высотой до 1 м, с 4-6, иногда 6-8 стеблями, количество которых зависит от сорта и величины посадочного клубня.	\N
5	капуста	Капустный кочан представляет собой стебель-кочерыгу, на которой расположены плотно свитые листья, прикрывающие боковые и верхушечную ростовые почки. Кочаны белокочанной капусты могут быть круглыми, округло-плоскими, конусовидными. Кочерыга может входить в кочан на глубину от 1/3 до 2/3 его толщины.	\N
6	морковь	Морковь – двухлетнее (редко однолетнее или многолетнее) растение, относящееся к царству растения, отделу цветковые, классу двудольные, порядку зонтикоцветные, семейству зонтичные, роду морковь (Daucus).	\N
10	огурец	Огурец отличается системой корневания стержневого типа, при этом корень способен проникать на глубину от 0,8 до 1,2 м. Основная часть корневища разветвлена в верхнем, наиболее плодородном, слое грунта от 0,1 до 0,4 м, благодаря чему растение может хорошо прогреваться на всех уровнях и впитывать питательные вещества.	Рекомендуется выращивать в пленочных теплицах или на открытом грунте в теплых регионах.
7	помидор	Стебель помидоров состоит из части стеблей боковых побегов; травянистый, сочный, во влажной почве легко образует дополнительные корни. По мере роста они грубеют. Имеет округлую форму, с возрастом изменяется в результате образования на нем желобов и становится ребристым.	Рекомендуется выращивать в пленочных теплицах или на открытом грунте в теплых регионах.
8	семена картошки	Ранний сорт немецкой селекции с желтой мякотью. Урожайный, относительно неприхотливый, устойчивый к картофельным болезням. Универсального кулинарного типа,  средней развариваемости.	Рекомендуется выращивать в пленочных теплицах или на открытом грунте в теплых регионах.
9	семена помидора	Сорт среднего созревания, любимец среди садоводов. Культура, даже при плохих условиях, дает прекрасный урожай. Растение достигает высоты 1,5-1,8 м. Спелые плоды имеют красный окрас, мясистые, сердцевидной формы. Их масса достигает до 500 г.	Рекомендуется выращивать в пленочных теплицах или на открытом грунте в теплых регионах.
\.



--
-- TOC entry 3376 (class 0 OID 17038)
-- Dependencies: 210
-- Data for Name: Position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Position" ("idPosition", "namePosition", salary, note) FROM stdin;
1	salesman	3600	\N
2	administrator	4600	\N
3	loader	2500	\N
4	driver	3000	\N
5	accountant	3000	\N
6	agronomist	3000	\N
\.



--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 216
-- Name: Client_idClient_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Client_idClient_seq"', 0, true);


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 219
-- Name: Employee_idEmployee_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Employee_idEmployee_seq"', 0, true);


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 218
-- Name: Goods_idGoods_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Goods_idGoods_seq"', 10, true);


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 221
-- Name: Payments_idPayments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Payments_idPayments_seq"', 0, true);


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 220
-- Name: Position_idPosition_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Position_idPosition_seq"', 6, true);


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 217
-- Name: User_idUser_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."User_idUser_seq"', 0, true);


--
-- TOC entry 3223 (class 2606 OID 17063)
-- Name: Client Client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Client"
    ADD CONSTRAINT "Client_pkey" PRIMARY KEY ("idClient");


--
-- TOC entry 3221 (class 2606 OID 17058)
-- Name: Employee Employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_pkey" PRIMARY KEY ("idEmployee");


--
-- TOC entry 3213 (class 2606 OID 17037)
-- Name: Goods Goods_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Goods"
    ADD CONSTRAINT "Goods_pkey" PRIMARY KEY ("idGoods");


--
-- TOC entry 3225 (class 2606 OID 17068)
-- Name: Payments Payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Payments"
    ADD CONSTRAINT "Payments_pkey" PRIMARY KEY ("idPayments");


--
-- TOC entry 3215 (class 2606 OID 17044)
-- Name: Position Position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Position"
    ADD CONSTRAINT "Position_pkey" PRIMARY KEY ("idPosition");


--
-- TOC entry 3217 (class 2606 OID 17051)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id_user);


--
-- TOC entry 3219 (class 2606 OID 25402)
-- Name: User login; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT login UNIQUE (login);


--
-- TOC entry 3228 (class 2606 OID 17082)
-- Name: Client Client_idUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Client"
    ADD CONSTRAINT "Client_idUser_fkey" FOREIGN KEY ("idUser") REFERENCES public."User"(id_user) NOT VALID;


--
-- TOC entry 3227 (class 2606 OID 17077)
-- Name: Employee Employee_idPosition_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_idPosition_fkey" FOREIGN KEY ("idPosition") REFERENCES public."Position"("idPosition") NOT VALID;


--
-- TOC entry 3226 (class 2606 OID 17072)
-- Name: Employee Employee_idUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_idUser_fkey" FOREIGN KEY ("idUser") REFERENCES public."User"(id_user) NOT VALID;


--
-- TOC entry 3229 (class 2606 OID 17087)
-- Name: Sales Sales_idClient_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sales"
    ADD CONSTRAINT "Sales_idClient_fkey" FOREIGN KEY ("idClient") REFERENCES public."Client"("idClient") NOT VALID;


--
-- TOC entry 3231 (class 2606 OID 17097)
-- Name: Sales Sales_idGoods_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sales"
    ADD CONSTRAINT "Sales_idGoods_fkey" FOREIGN KEY ("idGoods") REFERENCES public."Goods"("idGoods") NOT VALID;


--
-- TOC entry 3230 (class 2606 OID 17092)
-- Name: Sales Sales_idPayments_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sales"
    ADD CONSTRAINT "Sales_idPayments_fkey" FOREIGN KEY ("idPayments") REFERENCES public."Payments"("idPayments") NOT VALID;


--
-- TOC entry 3232 (class 2606 OID 17102)
-- Name: Sales Sales_idSaller_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sales"
    ADD CONSTRAINT "Sales_idSaller_fkey" FOREIGN KEY ("idSaller") REFERENCES public."Employee"("idEmployee") NOT VALID;


-- Completed on 2022-03-19 21:29:17

--
-- PostgreSQL database dump complete
--

