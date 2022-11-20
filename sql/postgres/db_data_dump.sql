--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

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
-- Data for Name: services; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.services (id, name, description) FROM stdin;
1	Internet	Fast internet - You will like it
3	Mobile network	The whole world in your pocket
2	TV	Do you really think you need it?
4	Dark magic	Your favourite necromancy
5	Satellite TV	More than 1000 channels
\.


--
-- Data for Name: service_translations; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.service_translations (service_id, locale, name, description) FROM stdin;
1	uk	Інтернет	Швидкий інтернет
2	uk	Телебачення	Ви ще не викинули ящик?
5	uk	Супутникове ТБ	Більше 1000 каналів
4	uk	Темна магія	Відтепер вам доступна некромантія!
3	uk	Мобільний інтернет	Цілий світ в вашій кишені
\.


--
-- Data for Name: tariffs; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.tariffs (id, title, description, status, usd_price, image_file_name) FROM stdin;
1	Internet	How can you live without it???	ACTIVE	10.00	1192569022img
2	TV Madness	Even more TV than you need	ACTIVE	1000.00	556852510connectingtotheinternet_image.png
3	Стартовий інтернет-пакет	Цей пакет вам точно потрібен!	ACTIVE	100.00	892007640internet.jpg
4	Sattelite TV	Kind of gripping description	ACTIVE	50.00	1512866863connectingtotheinternet_image.png
5	The most unusual tariff	Are you strong enough to carry this one?	ACTIVE	999999.00	1249973391magic.jpg
6	Mobile pack	Mobile network pack	ACTIVE	50.00	1943274817img
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.users (id, login, role, name, surname, phone, status) FROM stdin;
1	root	ROOT	root	root	000000	ACTIVE
3	user9	MEMBER	name9	surname9	9999999999	ACTIVE
4	user3	MEMBER	name3	surname3	3333333333	ACTIVE
5	user4	MEMBER	name4	surname4	4444444444	ACTIVE
6	user8	MEMBER	name8	surname8	8888888888	ACTIVE
7	user10	MEMBER	name10	surname10	1010101010	ACTIVE
8	user5	MEMBER	name5	surname5	5555555555	ACTIVE
9	user2	MEMBER	name2	surname2	2222222222	ACTIVE
10	user7	MEMBER	name7	surname7	7777777777	ACTIVE
11	user11	MEMBER	name11	surname11	1111111111	ACTIVE
12	qwerty	ADMIN	qwerty	qwerty	123456	SUSPENDED
2	user6	MEMBER	name6	surname6	6666666666	SUSPENDED
\.


--
-- Data for Name: user_accounts; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.user_accounts (id, user_id, currency, amount) FROM stdin;
2	2	USD	0.00
3	3	USD	0.00
4	4	USD	0.00
5	5	USD	0.00
6	6	USD	0.00
7	7	USD	0.00
8	8	USD	0.00
9	9	USD	0.00
10	10	USD	0.00
11	11	USD	0.00
12	12	USD	0.00
1	1	USD	840.00
\.


--
-- Data for Name: subscriptions; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.subscriptions (id, user_account_id, tariff_id, start_time, last_payment_time, status) FROM stdin;
1	1	1	2022-11-11 17:46:53.951444+02	2022-11-11 17:46:53.951444+02	INACTIVE
2	1	1	2022-11-11 18:17:53.607087+02	2022-11-11 20:37:19.89723+02	INACTIVE
3	1	1	2022-11-11 20:41:49.293068+02	2022-11-11 21:14:23.134882+02	INACTIVE
4	1	1	2022-11-11 21:16:31.851938+02	2022-11-13 12:44:07.882283+02	INACTIVE
5	1	2	2022-11-13 13:03:06.682139+02	2022-11-13 13:03:06.682139+02	INACTIVE
6	1	2	2022-11-13 13:03:28.881216+02	2022-11-18 19:46:12.778903+02	INACTIVE
8	1	1	2022-11-19 23:03:47.198508+02	2022-11-19 23:41:52.427277+02	INACTIVE
9	1	1	2022-11-19 23:42:04.640985+02	2022-11-20 00:06:41.598628+02	INACTIVE
7	1	2	2022-11-19 22:55:25.076342+02	2022-11-20 15:52:23.43875+02	INACTIVE
10	1	1	2022-11-20 00:06:56.552864+02	2022-11-20 16:22:17.725023+02	INACTIVE
13	1	3	2022-11-20 17:26:24.177727+02	2022-11-20 17:26:24.177727+02	INACTIVE
11	1	6	2022-11-20 17:26:14.859401+02	2022-11-20 17:26:14.859401+02	INACTIVE
12	1	1	2022-11-20 17:26:18.562441+02	2022-11-20 17:26:18.562441+02	INACTIVE
\.


--
-- Data for Name: tariff_durations; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.tariff_durations (tariff_id, months, minutes) FROM stdin;
1	0	10
2	0	3
3	1	0
4	1	0
5	0	60
6	0	60
\.


--
-- Data for Name: tariff_services; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.tariff_services (tariff_id, service_id) FROM stdin;
1	1
2	2
2	5
3	1
3	3
4	5
5	4
6	3
\.


--
-- Data for Name: tariff_translations; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.tariff_translations (tariff_id, locale, title, description) FROM stdin;
1	uk	Інтернет	Хіба можна без нього жити?
2	uk	ТБ безумство	Більше каналів, ніж тобі треба
4	uk	Супутникове телебачення	Типу дуже цікавий опис
5	uk	Самий незвичайний тариф	Ви впевнені, що вам це треба?
6	uk	Мобільний пакет	Пакет мобільного інтернету
\.


--
-- Data for Name: user_passwords; Type: TABLE DATA; Schema: public; Owner: ruslan
--

COPY public.user_passwords (user_id, hash, salt, hash_method) FROM stdin;
1	v80J7G7xaIxBL9Qxn+EMZw==	neR+ZEnXbA/TQ0D0E1QZPw==	PBKDF2_1
2	8Sk9kiMZFTKUNMYxvPumpQ==	V9pKA1BLNbyi/kA4msjxvg==	PBKDF2_1
3	wWNeXuLn3vCesFV47TExtA==	ordesvO9BvTuX//60T5cMA==	PBKDF2_1
4	XbZZN4KXrrQ0F7Up5/hqYw==	U0Q9M6CZoHmcXbZUZuGgxA==	PBKDF2_1
5	xWgt0DQQku11qNeqMenyaA==	73NyzYBh2zCY40OCiXzIbg==	PBKDF2_1
6	35czN60PrH1hV0DwY7hHWQ==	aC0AFpf1yqgnvKyJtFHsgw==	PBKDF2_1
7	C4Zfra6Dso8utDm0UGnRGQ==	QIwSkYNK1YvJCf5lQD6/bw==	PBKDF2_1
8	5uya1N+T3XBT7OrO5IdbLw==	+00mnwswXardYYpK0bgnIQ==	PBKDF2_1
9	1my0VlWUOP7GWOKeCh5Tjg==	eqTCBSPkNeCqT7g7sqBPXg==	PBKDF2_1
10	eG7ffCe7JtPS/UJIm4XBUQ==	phw2a43OutBFmHlQi72izQ==	PBKDF2_1
11	WMfZRK1vMu3zCG8YC1s3iw==	nc6eitgpZ82XUkefXd46Iw==	PBKDF2_1
12	m8tjsdhqnw+/g0UhDi7X8g==	hwA6cW5bNlNnZ0xTV7gqZw==	PBKDF2_1
\.


--
-- Name: services_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ruslan
--

SELECT pg_catalog.setval('public.services_id_seq', 6, true);


--
-- Name: subscriptions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ruslan
--

SELECT pg_catalog.setval('public.subscriptions_id_seq', 13, true);


--
-- Name: tariffs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ruslan
--

SELECT pg_catalog.setval('public.tariffs_id_seq', 6, true);


--
-- Name: user_accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ruslan
--

SELECT pg_catalog.setval('public.user_accounts_id_seq', 12, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ruslan
--

SELECT pg_catalog.setval('public.users_id_seq', 12, true);


--
-- PostgreSQL database dump complete
--

