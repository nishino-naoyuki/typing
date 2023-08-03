-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- ホスト: 127.0.0.1
-- 生成日時: 2023-08-03 07:20:11
-- サーバのバージョン： 10.4.27-MariaDB
-- PHP のバージョン: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- データベース: `typing`
--

-- --------------------------------------------------------

--
-- テーブルの構造 `ans_history_tbl`
--

CREATE TABLE `ans_history_tbl` (
  `anshid` int(11) NOT NULL,
  `ansid` int(11) NOT NULL,
  `submit_no` int(11) NOT NULL,
  `answer` varchar(16383) NOT NULL COMMENT '回答内容',
  `time` int(11) NOT NULL COMMENT '解答時間',
  `ans_timestamp` datetime NOT NULL COMMENT '提出時間',
  `correct_flg` int(11) NOT NULL,
  `socre_json` varchar(4000) NOT NULL COMMENT '採点結果のJSONデータ',
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `ans_history_tbl`
--

INSERT INTO `ans_history_tbl` (`anshid`, `ansid`, `submit_no`, `answer`, `time`, `ans_timestamp`, `correct_flg`, `socre_json`, `score`) VALUES
(1, 4, 1, '練習問題１です', 3, '2023-03-09 17:50:39', 0, '{\"totalScore\":240,\"accuracyScore\":100,\"sppedScore\":140.0,\"unjustFlag\":false}', 240),
(2, 5, 1, '練習問題１です', 5, '2023-03-09 17:51:01', 0, '{\"totalScore\":169,\"accuracyScore\":85,\"sppedScore\":84.0,\"unjustFlag\":false}', 169),
(3, 4, 2, '練習問題１ですあ', 5, '2023-03-09 17:51:19', 0, '{\"totalScore\":183,\"accuracyScore\":87,\"sppedScore\":96.0,\"unjustFlag\":false}', 183),
(4, 6, 1, '本番用の問題１でｓ', 7, '2023-03-09 18:15:38', 0, '{\"totalScore\":165,\"accuracyScore\":88,\"sppedScore\":77.14285714285714,\"unjustFlag\":false}', 165),
(5, 7, 1, '本番用の問題４です', 16, '2023-03-09 18:24:06', 0, '{\"totalScore\":133,\"accuracyScore\":100,\"sppedScore\":33.75,\"unjustFlag\":false}', 133),
(6, 7, 2, '本番用の問題４です', 6, '2023-03-09 18:24:36', 0, '{\"totalScore\":190,\"accuracyScore\":100,\"sppedScore\":90.0,\"unjustFlag\":false}', 190),
(7, 8, 1, '本番用の問題３です', 9, '2023-03-09 18:25:33', 0, '{\"totalScore\":160,\"accuracyScore\":100,\"sppedScore\":60.0,\"unjustFlag\":false}', 160),
(8, 9, 1, '', 6, '2023-03-17 14:52:19', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":0.0,\"unjustFlag\":false}', 0),
(9, 9, 2, '', 3, '2023-03-17 15:11:36', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":0.0,\"unjustFlag\":false}', 0),
(10, 9, 3, '', 0, '2023-03-17 15:11:40', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":\"NaN\",\"unjustFlag\":false}', 0),
(11, 9, 4, '', 46, '2023-03-17 15:12:31', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":0.0,\"unjustFlag\":false}', 0),
(12, 9, 5, '', 14, '2023-03-17 15:12:51', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":0.0,\"unjustFlag\":false}', 0),
(13, 9, 6, '', 6, '2023-03-17 15:14:20', 0, '{\"totalScore\":0,\"accuracyScore\":0,\"sppedScore\":0.0,\"unjustFlag\":false}', 0),
(14, 9, 7, 'undefined', 88, '2023-03-17 16:18:53', 0, '{\"totalScore\":6,\"accuracyScore\":0,\"sppedScore\":6.136363636363637,\"unjustFlag\":false}', 6),
(15, 9, 8, 'undefined', 21, '2023-03-17 16:48:00', 0, '{\"totalScore\":25,\"accuracyScore\":0,\"sppedScore\":25.714285714285715,\"unjustFlag\":false}', 25),
(16, 9, 9, 'ニュートン祭になぜ林檎を飾るかといえば、それはニュートンが林檎の実の落ちるのを見て万有引力を発見したという有名な話があるからです。この話の由来について少しばかりせつめいｓていみあまｓ', 168, '2023-03-17 16:51:43', 0, '{\"totalScore\":40,\"accuracyScore\":8,\"sppedScore\":32.5,\"unjustFlag\":false}', 40),
(17, 9, 10, 'ニュートン祭になぜ林檎を飾るかといえば、それはニュートンが林檎の実の落ちるのを見て万有引力を発見したという有名な話があるからです。この話の由来について少しばかり説明してみます', 52, '2023-03-17 17:15:06', 0, '{\"totalScore\":108,\"accuracyScore\":8,\"sppedScore\":100.38461538461539,\"unjustFlag\":false}', 108),
(18, 10, 1, '1行目\r\n2行目\r\n3行目\r\n4行目', 11, '2023-07-19 16:16:30', 0, '{\"totalScore\":198,\"accuracyScore\":100,\"sppedScore\":98.18181818181819,\"unjustFlag\":false}', 198),
(19, 10, 2, '1行目\r\n2行目\r\n3行目\r\n4行目', 18, '2023-07-19 16:28:12', 0, '{\"totalScore\":160,\"accuracyScore\":100,\"sppedScore\":60.0,\"unjustFlag\":false}', 160),
(20, 11, 1, '本番用の問題１です', 8, '2023-07-27 10:18:54', 0, '{\"totalScore\":167,\"accuracyScore\":100,\"sppedScore\":67.5,\"unjustFlag\":false}', 167),
(21, 10, 3, '1行目\r\n2行目\r\n3行目\r\n4行目', 12, '2023-07-27 19:36:02', 0, '{\"totalScore\":190,\"accuracyScore\":100,\"sppedScore\":90.0,\"unjustFlag\":false}', 190),
(22, 12, 1, '本番用の問題１です', 8, '2023-07-27 19:48:13', 0, '{\"totalScore\":167,\"accuracyScore\":100,\"sppedScore\":67.5,\"unjustFlag\":false}', 167),
(23, 13, 1, '練習問題４ああああああ', 9, '2023-07-27 19:49:05', 0, '{\"totalScore\":173,\"accuracyScore\":100,\"sppedScore\":73.33333333333334,\"unjustFlag\":false}', 173),
(24, 14, 1, '練習問題２です', 7, '2023-07-27 19:51:05', 0, '{\"totalScore\":160,\"accuracyScore\":100,\"sppedScore\":60.0,\"unjustFlag\":false}', 160),
(25, 14, 2, '練習問題２です', 11, '2023-07-27 19:51:49', 0, '{\"totalScore\":138,\"accuracyScore\":100,\"sppedScore\":38.18181818181819,\"unjustFlag\":false}', 138),
(26, 10, 4, '1行目\r\n2行目\r\n3行目\r\n4行目', 34, '2023-07-31 20:24:05', 0, '{\"totalScore\":131,\"accuracyScore\":100,\"sppedScore\":31.764705882352942,\"unjustFlag\":false}', 131),
(27, 10, 5, '1行目\r\n2行目\r\n3行目\r\n4行目', 12, '2023-07-31 20:28:38', 0, '{\"totalScore\":190,\"accuracyScore\":100,\"sppedScore\":90.0,\"unjustFlag\":false}', 190),
(28, 10, 6, '1行目\r\n2行目\r\n3行目\r\n4行目', 1085, '2023-08-02 16:51:59', 0, '{\"totalScore\":100,\"accuracyScore\":100,\"sppedScore\":0.9953917050691244,\"unjustFlag\":false}', 100),
(29, 10, 7, '1行目\r\n2行目\r\n3行目\r\n4行目', 1831, '2023-08-02 17:04:25', 0, '{\"totalScore\":100,\"accuracyScore\":100,\"sppedScore\":0.5898416166029492,\"unjustFlag\":false}', 100),
(30, 15, 1, '本番用の問題１です', 32, '2023-08-02 17:35:46', 1, '{\"totalScore\":0,\"accuracyScore\":100,\"sppedScore\":16.875,\"unjustFlag\":true}', 0),
(31, 15, 2, '本番用の問題１です\r\n', 39, '2023-08-02 17:37:31', 0, '{\"totalScore\":116,\"accuracyScore\":100,\"sppedScore\":16.923076923076923,\"unjustFlag\":false}', 116);

-- --------------------------------------------------------

--
-- テーブルの構造 `ans_tbl`
--

CREATE TABLE `ans_tbl` (
  `ansid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `eqid` int(11) NOT NULL,
  `score` int(11) NOT NULL COMMENT '得点\r\n最後の１回分のみ',
  `submit_count` int(11) NOT NULL COMMENT '提出回数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `ans_tbl`
--

INSERT INTO `ans_tbl` (`ansid`, `uid`, `eqid`, `score`, `submit_count`) VALUES
(4, 1, 1, 183, 2),
(5, 1, 2, 169, 1),
(6, 5, 5, 165, 1),
(7, 3, 4, 190, 2),
(8, 5, 6, 160, 1),
(9, 1, 34, 108, 10),
(10, 1, 35, 100, 7),
(11, 15, 37, 167, 1),
(12, 2, 37, 167, 1),
(13, 2, 12, 173, 1),
(14, 2, 2, 138, 2),
(15, 3, 19, 116, 2);

-- --------------------------------------------------------

--
-- テーブルの構造 `ans_temp_tbl`
--

CREATE TABLE `ans_temp_tbl` (
  `token` varchar(64) NOT NULL COMMENT 'トークン',
  `start_time` datetime DEFAULT NULL COMMENT '開始時刻',
  `uid` int(11) NOT NULL,
  `eqid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `ans_temp_tbl`
--

INSERT INTO `ans_temp_tbl` (`token`, `start_time`, `uid`, `eqid`) VALUES
('1908cdbb2ccec766f5bad885d2ac650e', '2023-08-02 17:37:46', 3, 35),
('69fcc6f9483895e9bd0158966cf9af89', NULL, 1, 39),
('9a79e05df7e799f263cfca56d80cb0ce', '2023-08-02 17:22:47', 1, 19),
('ef50979c8a9deda8f21d09bb3b77a978', NULL, 1, 20),
('f0ce190bdceb24626723498f2d775833', '2023-08-02 17:10:15', 1, 35);

-- --------------------------------------------------------

--
-- テーブルの構造 `event_question`
--

CREATE TABLE `event_question` (
  `eqid` int(11) NOT NULL,
  `no` int(11) NOT NULL COMMENT 'イベント内での問題番号',
  `eid` int(11) DEFAULT NULL COMMENT '練習問題の場合はNULLで登録',
  `qid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `event_question`
--

INSERT INTO `event_question` (`eqid`, `no`, `eid`, `qid`) VALUES
(1, 0, NULL, 1),
(2, 0, NULL, 2),
(4, 2, 1, 7),
(5, 6, 1, 4),
(6, 1, 1, 6),
(7, 3, 1, 8),
(8, 5, 1, 5),
(9, 1, 2, 7),
(10, 2, 2, 6),
(11, 3, 2, 5),
(12, 0, NULL, 9),
(14, 4, 1, 3),
(15, 1, 3, 4),
(18, 1, 5, 4),
(19, 1, 6, 4),
(20, 2, 6, 5),
(21, 1, 7, 4),
(22, 2, 7, 5),
(23, 1, 8, 4),
(24, 2, 8, 5),
(26, 1, 9, 4),
(27, 2, 9, 5),
(28, 3, 9, 6),
(29, 4, 9, 7),
(30, 5, 9, 8),
(31, 6, 9, 3),
(32, 1, 10, 4),
(33, 1, 11, 4),
(34, 0, NULL, 10),
(35, 0, NULL, 18),
(36, 1, 12, 16),
(37, 2, 12, 4),
(38, 3, 12, 12),
(39, 3, 6, 6);

-- --------------------------------------------------------

--
-- テーブルの構造 `event_tbl`
--

CREATE TABLE `event_tbl` (
  `eid` int(11) NOT NULL,
  `name` varchar(256) NOT NULL COMMENT 'イベント名',
  `public_date` datetime NOT NULL COMMENT '公開日時',
  `start_date` datetime NOT NULL COMMENT 'イベントの開始日',
  `finish_date` datetime NOT NULL COMMENT '終了日時',
  `public_end_date` datetime NOT NULL COMMENT '公開終了日時',
  `ower_name` varchar(256) NOT NULL COMMENT '主催者名',
  `rankingdisplay` int(11) NOT NULL,
  `hiderankingtime` int(11) NOT NULL,
  `filter` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `event_tbl`
--

INSERT INTO `event_tbl` (`eid`, `name`, `public_date`, `start_date`, `finish_date`, `public_end_date`, `ower_name`, `rankingdisplay`, `hiderankingtime`, `filter`) VALUES
(1, 'イベント１aaa', '2023-03-08 17:53:00', '2023-03-09 17:53:00', '2023-03-31 17:53:00', '9999-12-31 23:59:00', '西野　直幸aaaaaaaaa', 0, 51, '.*'),
(2, 'イベント2', '2023-03-08 17:53:00', '2023-03-10 17:53:00', '2023-03-31 17:53:00', '9999-12-31 23:59:00', '西野　直幸', 0, 5, '.*'),
(3, 'イベント３', '2023-03-31 11:20:00', '2023-04-01 11:20:00', '2023-04-30 11:20:00', '9999-12-31 23:59:00', 'テスト', 0, 10, '.*'),
(5, '終了している奴', '2023-03-10 11:21:00', '2023-03-10 11:21:00', '2023-03-31 11:21:00', '9999-12-31 23:59:00', 'aaaaa', 0, 0, '.*'),
(6, 'いべんとこうしん', '2023-03-10 11:33:00', '2023-08-02 17:35:00', '2023-08-12 23:35:00', '9999-12-31 23:59:00', '西野', 0, 0, '.*'),
(7, 'aaaaa', '2023-03-10 11:33:00', '2023-03-10 11:33:00', '2023-03-10 11:33:00', '9999-12-31 23:59:00', 'aaaaa', 0, 0, '.*'),
(8, 'aaaaa', '2023-03-10 11:33:00', '2023-03-10 11:33:00', '2023-03-10 11:33:00', '9999-12-31 23:59:00', 'aaaaa', 0, 0, '.*'),
(9, 'フィルターテストSだけ', '2023-03-10 12:49:00', '2023-03-13 12:49:00', '2023-03-31 12:49:00', '9999-12-31 23:59:00', '西野です', 0, 10, '.*@s.asojuku.ac.jp'),
(10, 'aaaaa', '2023-05-26 17:17:00', '2023-03-23 17:17:00', '2023-03-30 17:17:00', '9999-12-31 23:59:00', 'bbb', 0, 1, '.*'),
(11, 'asdfsdafsadfas', '2023-03-10 17:20:00', '2023-03-17 17:20:00', '2023-03-17 17:20:00', '9999-12-31 23:59:00', 'bb', 1, 1, '.*'),
(12, 'フィルタ', '2023-07-26 17:13:00', '2023-07-27 10:41:00', '2023-07-30 17:13:00', '9999-12-31 23:59:00', '西野', 0, 0, '.*');

-- --------------------------------------------------------

--
-- テーブルの構造 `event_user`
--

CREATE TABLE `event_user` (
  `euid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `eid` int(11) NOT NULL,
  `del_flg` int(11) NOT NULL COMMENT '削除フラグ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `event_user`
--

INSERT INTO `event_user` (`euid`, `uid`, `eid`, `del_flg`) VALUES
(1, 2, 1, 0),
(2, 2, 2, 0),
(3, 3, 2, 0),
(4, 3, 1, 0),
(5, 4, 1, 0),
(6, 5, 1, 0),
(7, 5, 2, 0),
(8, 12, 9, 0),
(9, 12, 2, 0),
(10, 15, 12, 0),
(11, 3, 12, 0),
(12, 2, 12, 0),
(13, 4, 12, 0),
(14, 3, 6, 0);

-- --------------------------------------------------------

--
-- テーブルの構造 `qestion_tbl`
--

CREATE TABLE `qestion_tbl` (
  `qid` int(11) NOT NULL,
  `title` varchar(256) NOT NULL COMMENT 'タイトル',
  `sentence` varchar(16383) NOT NULL COMMENT '問題の文章',
  `practiceflg` int(11) NOT NULL COMMENT '0:本番用問題\r\n1:練習用問題',
  `difficalty` int(11) NOT NULL COMMENT '難易度',
  `count` int(11) NOT NULL COMMENT '問題文の文字数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- テーブルの構造 `question_tbl`
--

CREATE TABLE `question_tbl` (
  `qid` int(11) NOT NULL,
  `title` varchar(256) NOT NULL COMMENT 'タイトル',
  `sentence` varchar(16383) NOT NULL COMMENT '問題の文章',
  `practiceflg` int(11) NOT NULL COMMENT '0:本番用問題\r\n1:練習用問題',
  `difficalty` int(11) NOT NULL COMMENT '難易度',
  `count` int(11) NOT NULL COMMENT '問題文の文字数',
  `create_date` datetime NOT NULL COMMENT '問題の作成日',
  `update_date` datetime NOT NULL COMMENT '更新日'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `question_tbl`
--

INSERT INTO `question_tbl` (`qid`, `title`, `sentence`, `practiceflg`, `difficalty`, `count`, `create_date`, `update_date`) VALUES
(1, '練習問題１', '練習問題１です', 1, 1, 7, '2023-03-09 17:34:28', '2023-03-09 17:34:28'),
(2, '練習問題２', '練習問題２です', 1, 1, 7, '2023-03-09 17:34:37', '2023-03-09 17:34:37'),
(3, '練習問題３から本番用の問題へ', '練習問題３です', 0, 2, 7, '2023-03-09 17:34:46', '2023-03-09 20:00:46'),
(4, '本番用の問題１', '本番用の問題１です', 0, 1, 9, '2023-03-09 17:52:37', '2023-03-09 17:52:37'),
(5, '本番用の問題２', '本番用の問題２です', 0, 1, 9, '2023-03-09 17:52:46', '2023-03-09 17:52:46'),
(6, '本番用の問題３', '本番用の問題３です', 0, 1, 9, '2023-03-09 17:52:54', '2023-03-09 17:52:54'),
(7, '本番用の問題４', '本番用の問題４です', 0, 1, 9, '2023-03-09 17:53:01', '2023-03-09 17:53:01'),
(8, '本番用の問題５', 'ニュートン祭になぜ林檎を飾るかといえば、それはニュートンが林檎の実の落ちるのを見て万有引力を発見したという有名な話があるからです。この話の由来について少しばかり説明してみますと、次の通りです。\r\n　ニュートンの名はアイザックと言いますが、その生まれた故郷は、イギリスの中部にあるリンコルン領地の中のウールスソープという小さな村でした。その村で小学校を卒業してから隣町の中学校に入ったところ、家庭の事情で一年ばかり経って家に呼び戻され、農業に従事することになりました。それというのも父はアイザックの生まれる前に病気で死んでしまい、母親は一旦他の家に嫁いだのに、そこでまた夫と死別してニュートンの生家に帰ってきたからでした。しかしアイザックがいかにも学問好きなので、そのまま農業をさせておくのも惜しいと人々に忠告されて、ともかく中学校を続けさせることになったのですが、成績も非常に良かったので、卒業後はもう少し学問を大成させようということになり、１８歳の折にケンブリッジ大学に入学しました。\r\n　大学では数学や物理学を修め、１６６５年に優秀な成績で卒業し、そのまま大学に留まってなお研究を続けていました。ところがその頃のヨーロッパにはペスト病が激しく流行し、恐ろしく多数の死者を出すという有様でした。ちょうど翌年の夏にはイギリスがその流行に襲われたので、ケンブリッジ大学もしばらくの間閉鎖して、学生はみんな郷里へ帰ることになりました。それでニュートンも故郷に戻ったのですが、その間にも自分の好きな研究は少しも怠りませんでした。そのときの研究というのが、ちょうど星の運動であったのです。つまり星の運動はどんな力に支配されているのかという問題を深く考えていたのですが、ある日庭園を散歩してみると、ふと林檎の実が枝からぼたりと落ちたのを見て、それで万有引力ということに気がついたというのです。\r\n　この話は、ニュートンが死んでから十年程後に出版されたヴォルテールという人の著書の中に、ニュートンの姪から聞いたものとして記されているので、その後伝えられて有名になったのですが、ニュートンが本当に林檎の実から引力を思いついたということは、はなはだ疑わしいのです。ニュートンの家の庭園に林檎の樹が確かにあったという説があったり、またその樹の幹の一部だといわれるものがある博物館に保存されてもいますけれども、それでも話の筋道がどうもこれだけでははっきりしないのです。', 0, 3, 1024, '2023-03-09 17:53:29', '2023-03-09 17:53:29'),
(9, '練習問題５', '練習問題４ああああああ', 1, 5, 11, '2023-03-09 19:25:59', '2023-03-09 20:00:24'),
(10, '長文', 'ニュートン祭になぜ林檎を飾るかといえば、それはニュートンが林檎の実の落ちるのを見て万有引力を発見したという有名な話があるからです。この話の由来について少しばかり説明してみますと、次の通りです。\r\nニュートンの名はアイザックと言いますが、その生まれた故郷は、イギリスの中部にあるリンコルン領地の中のウールスソープという小さな村でした。その村で小学校を卒業してから隣町の中学校に入ったところ、家庭の事情で一年ばかり経って家に呼び戻され、農業に従事することになりました。それというのも父はアイザックの生まれる前に病気で死んでしまい、母親は一旦他の家に嫁いだのに、そこでまた夫と死別してニュートンの生家に帰ってきたからでした。しかしアイザックがいかにも学問好きなので、そのまま農業をさせておくのも惜しいと人々に忠告されて、ともかく中学校を続けさせることになったのですが、成績も非常に良かったので、卒業後はもう少し学問を大成させようということになり、１８歳の折にケンブリッジ大学に入学しました。\r\n大学では数学や物理学を修め、１６６５年に優秀な成績で卒業し、そのまま大学に留まってなお研究を続けていました。ところがその頃のヨーロッパにはペスト病が激しく流行し、恐ろしく多数の死者を出すという有様でした。ちょうど翌年の夏にはイギリスがその流行に襲われたので、ケンブリッジ大学もしばらくの間閉鎖して、学生はみんな郷里へ帰ることになりました。それでニュートンも故郷に戻ったのですが、その間にも自分の好きな研究は少しも怠りませんでした。そのときの研究というのが、ちょうど星の運動であったのです。つまり星の運動はどんな力に支配されているのかという問題を深く考えていたのですが、ある日庭園を散歩してみると、ふと林檎の実が枝からぼたりと落ちたのを見て、それで万有引力ということに気がついたというのです。\r\nこの話は、ニュートンが死んでから十年程後に出版されたヴォルテールという人の著書の中に、ニュートンの姪から聞いたものとして記されているので、その後伝えられて有名になったのですが、ニュートンが本当に林檎の実から引力を思いついたということは、はなはだ疑わしいのです。ニュートンの家の庭園に林檎の樹が確かにあったという説があったり、またその樹の幹の一部だといわれるものがある博物館に保存されてもいますけれども、それでも話の筋道がどうもこれだけでははっきりしないのです。\r\n', 1, 1, 1023, '2023-03-17 14:18:46', '2023-03-17 14:18:46'),
(11, '練習問題１', '練習問題１です\r\n変更', 1, 1, 11, '2023-03-10 16:57:55', '2023-03-10 16:58:23'),
(12, '本番用の問題１', '本番用の問題１です', 0, 2, 9, '2023-03-10 16:58:52', '2023-03-10 16:58:52'),
(13, '本番用の問題２', '本番用の問題２です', 0, 2, 9, '2023-03-10 16:59:01', '2023-03-10 16:59:01'),
(14, '公立小学校教員の残業代訴訟 最高裁上告退ける 教員の敗訴確定', '長時間労働を強いられたのに残業代が支払われないのは違法だとして、埼玉県内の公立小学校の教諭が、県に時間外勤務の賃金などの支払いを求めた裁判で、最高裁判所は上告を退け教諭の敗訴が確定しました。\r\n埼玉県内の公立小学校に勤務する60代の男性教諭は、時間外勤務をしたのに残業代が支払われないのは違法だと主張して、未払いの賃金を支払うよう県に求めました。公立学校の教職員の給与は、月給の4％分が上乗せされる代わりに、残業代は支給されないことが「給特法」という法律で定められていますが、教諭は「実態に合っていない」と訴えていました。1審のさいたま地方裁判所は「残業しないと業務が終わらない状況が常態化していたとはいえない」と判断して訴えは退けましたが「多くの教員が一定の時間外勤務に従事せざるをえない状況で、法律が教育現場の実情に合っていないのではないかと思わざるをえない」と指摘し、法律や給与体系の見直しなどの必要性に言及しました。\r\n2審の東京高等裁判所も訴えを退け、教諭は不服として上告していましたが、最高裁判所第2小法廷の岡村和美裁判長は10日までに退ける決定をし、敗訴が確定しました。教員の働き方の問題を問いかけた裁判で、司法の判断が注目されましたが、訴えは認められませんでした。「給特法」をめぐって文部科学省は去年、有識者会議を設置し、今後、見直しについて議論する方針です。', 1, 1, 588, '2023-03-10 19:14:44', '2023-03-10 19:14:44'),
(15, '公立小学校教員の残業代訴訟 最高裁上告退ける 教員の敗訴確定', '長時間労働を強いられたのに残業代が支払われないのは違法だとして、埼玉県内の公立小学校の教諭が、県に時間外勤務の賃金などの支払いを求めた裁判で、最高裁判所は上告を退け教諭の敗訴が確定しました。\r\n埼玉県内の公立小学校に勤務する60代の男性教諭は、時間外勤務をしたのに残業代が支払われないのは違法だと主張して、未払いの賃金を支払うよう県に求めました。公立学校の教職員の給与は、月給の4％分が上乗せされる代わりに、残業代は支給されないことが「給特法」という法律で定められていますが、教諭は「実態に合っていない」と訴えていました。1審のさいたま地方裁判所は「残業しないと業務が終わらない状況が常態化していたとはいえない」と判断して訴えは退けましたが「多くの教員が一定の時間外勤務に従事せざるをえない状況で、法律が教育現場の実情に合っていないのではないかと思わざるをえない」と指摘し、法律や給与体系の見直しなどの必要性に言及しました。\r\n2審の東京高等裁判所も訴えを退け、教諭は不服として上告していましたが、最高裁判所第2小法廷の岡村和美裁判長は10日までに退ける決定をし、敗訴が確定しました。教員の働き方の問題を問いかけた裁判で、司法の判断が注目されましたが、訴えは認められませんでした。「給特法」をめぐって文部科学省は去年、有識者会議を設置し、今後、見直しについて議論する方針です。', 1, 1, 588, '2023-03-10 19:14:54', '2023-03-10 19:14:54'),
(16, 'インフルエンザ3週連続減少も「注意報レベル」6週連続で上回る', '全国のインフルエンザの患者数は今月5日までの1週間では、推計でおよそ28万9000人と3週連続で前の週より減少しました。ただ、1医療機関当たりの患者数は大きな流行が起きる可能性があるとされる「注意報レベル」の水準を6週連続で上回っていて、専門家は引き続き注意を呼びかけています。国立感染症研究所などによりますと、今月5日までの1週間に全国およそ5000か所の医療機関から報告されたインフルエンザの患者数は、前の週から5638人減って5万235人でした。1医療機関当たりの1週間の患者数は10.17人で3週連続で前の週より減少したものの「注意報レベル」の水準の10人を6週連続で上回っています。また、ここから推計される全国の1週間の患者数はおよそ28万9000人となっています。', 0, 2, 338, '2023-03-10 19:16:42', '2023-03-10 19:16:42'),
(17, '日本経済の再生　次世代担う人への投資を', '社説：日本経済の再生 次世代担う人への投資を\r\n国力低下の表れではないか。昨年の歴史的な円安と物価高は国民生活や企業活動を直撃し、日本経済のもろさを浮き彫りにした。\r\nウクライナ危機に伴うエネルギー価格の高騰で、輸入額が急拡大した。一方「メード・イン・ジャパン」の売り物は乏しく、貿易赤字は過去最大に膨らんでいる。経済政策や産業のあり方を根本から問い直す時期に来ている。\r\nバブル経済の崩壊以降、日本は長らく低成長とデフレにあえいできた。「失われた２０年」にどう対応すべきか。鳴り物入りで登場したのがアベノミクスだった。\r\n２０１２年末の政権交代後、当時の安倍晋三首相は「最大かつ喫緊の課題は経済の再生」と訴えた。大胆な金融政策、機動的な財政政策、民間投資を喚起する成長戦略という「三本の矢」で経済を立て直す考えを表明した。\r\n中核となったのが、市場に大量のマネーを流し込む異次元の金融緩和だ。日銀の黒田東彦総裁は「２年程度で２％の物価安定目標を達成できる」と豪語した。\r\n円安と株高を演出し、グローバル企業の業績を押し上げたのは確かだ。しかし、企業は稼いだ利益で内部留保を積み上げ、成長を求めて海外への投資に力を注いだ。\r\n産業空洞化の流れは止まらず、経済の好循環は実現していない。とりわけ問題なのは、恩恵が大企業や富裕層などに偏り、幅広い働き手に及んでいない点だ。\r\n賃金は伸び悩み、非正規労働者は増え続けた。企業は２０００万人を超える非正規を、雇用の調整弁にしている。\r\n異次元の緩和が、財政規律を失わせていることも見逃せない。\r\n新型コロナウイルスの感染拡大を受け、政府は景気対策を繰り返し、国債発行額は、２０年度に１００兆円を超えた。超低金利で、利払い費が抑えられてきたことが背景にある。防衛費も大幅に増額し、異次元の財政悪化を招いている。\r\nもはやアベノミクスの限界は明らかだ。金融政策を見直し、財政運営を正常化すべきだ。そのうえで、日本経済を支える新たな産業の創出を急ぐ必要がある。\r\n求められるのは、地球規模の課題に取り組み、ライフスタイルを変えるような事業への挑戦だ。\r\nソニーグループは、ホンダと組み、環境車として本命視される電気自動車の開発に乗り出した。２５年に最初の車を発売する。\r\n合弁会社の社長には、ゲーム機や犬型ロボット、携帯電話などの開発に携わってきた人材を起用した。音楽や映像、センサーなど多分野の技術を結集し、「移動空間をエンターテインメントの空間にする」ことを目指す。', 1, 1, 1053, '2023-05-18 18:37:24', '2023-05-19 08:52:12'),
(18, '改行テスト', '1行目\r\n2行目\r\n3行目\r\n4行目', 1, 1, 18, '2023-07-19 16:05:25', '2023-07-19 16:05:25');

-- --------------------------------------------------------

--
-- テーブルの構造 `user_tbl`
--

CREATE TABLE `user_tbl` (
  `uid` int(11) NOT NULL,
  `mail` varchar(256) NOT NULL COMMENT 'メールアドレス\r\nログインアカウント',
  `password` varchar(256) NOT NULL,
  `name` varchar(256) NOT NULL COMMENT '名前',
  `disp_name` varchar(256) NOT NULL COMMENT '表示名',
  `affiliation` varchar(256) DEFAULT NULL COMMENT '所属\r\n学校名やクラス名など',
  `role` int(11) NOT NULL COMMENT '役割\r\n0:学生\r\n1:管理者',
  `del_flg` int(11) NOT NULL COMMENT '削除フラグ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- テーブルのデータのダンプ `user_tbl`
--

INSERT INTO `user_tbl` (`uid`, `mail`, `password`, `name`, `disp_name`, `affiliation`, `role`, `del_flg`) VALUES
(1, 'nishino@asojuku.ac.jp', 'ad19081ee19c812af599f5b183b90dab', '西野　直幸', '西野', '教員', 1, 0),
(2, '0000000@asojuku.ac.jp', '77010e230806c630508d317fd42118fa', 'テストユーザー1　太郎', 'テスト1さん', 'テストユーザー', 0, 0),
(3, '0000001@asojuku.ac.jp', '9836a710f92f6e0124ed6e945d43ccfe', 'テストユーザー2　太郎', 'テスト2さん', 'テストユーザー', 0, 0),
(4, '0000002@asojuku.ac.jp', '6ab83f639adf9e148aa52f8f5b9d6b8b', 'テストユーザー3　太郎', 'テスト3さん', 'テストユーザー', 0, 0),
(5, '0000003@asojuku.ac.jp', '18e1fccfea0e4b865f565e4988804ba3', 'テストユーザー4　太郎', 'テスト4さん', 'テストユーザー', 0, 0),
(6, '0000004@asojuku.ac.jp', 'd63edd8f744c2eacaf6560a6a8931ce2', 'テストユーザー5　太郎', 'テスト5さん', 'テストユーザー', 0, 0),
(7, '0000005@asojuku.ac.jp', 'cc73bce004ebc4831ca9371a66245fcb', 'テストユーザー6　太郎', 'テスト6さん', 'テストユーザー', 0, 0),
(8, '0000006@asojuku.ac.jp', '667047fba5426452577319ba6fb823c2', 'テストユーザー7　太郎', 'テスト7さん', 'テストユーザー', 0, 0),
(9, '0000007@asojuku.ac.jp', 'fb1b1792d89271ce18c25b9ad5efc228', 'テストユーザー8　太郎', 'テスト8さん', 'テストユーザー', 0, 0),
(10, '0000008@asojuku.ac.jp', 'a1ef364768d8060715b5f25ae414694e', 'テストユーザー9　太郎', 'テスト9さん', 'テストユーザー', 0, 0),
(11, '0000009@asojuku.ac.jp', 'ddeb312037d0eb697f5430d8e9b602f1', 'テストユーザー10　太郎', 'テスト10さん', 'テストユーザー', 0, 0),
(12, '0000001@s.asojuku.ac.jp', '95f2e301dd38dd06a415631f86e9fff1', 'テストユーザーS1　太郎', 'テストS1さん', 'Sテストユーザー', 0, 0),
(13, '0000002@s.asojuku.ac.jp', '29b7410f092e8bd7c2b31ceb67389f39', 'テストユーザーS2　太郎', 'テストS2さん', 'Sテストユーザー', 0, 0),
(14, '0000003@s.asojuku.ac.jp', 'c5100e49396ef50db3556e1bbd7262a7', 'テストユーザーS3　太郎', 'テストS3さん', 'Sテストユーザー', 0, 0),
(15, '2201571@s.asojuku.ac.jp', '3e5556e12bcfb140188d19bea6132dc6', '岩岡　璃桜', '岩岡　璃桜', 'テストユーザー', 0, 0),
(16, '2201572@s.asojuku.ac.jp', '1469062cf9f0d132dfd217e98e0d2d93', 'VU THI DUNG', 'VU THI DUNG', 'テストユーザー', 0, 0),
(17, '2201573@s.asojuku.ac.jp', '7db74f37e1ad56ce7b99225609532950', '大隈　渚月', '大隈　渚月', 'テストユーザー', 0, 0),
(18, '2201574@s.asojuku.ac.jp', 'da5912cd375044880c11d4f968d8b842', '太田　翼', '太田　翼', 'テストユーザー', 0, 0),
(19, '2201607@s.asojuku.ac.jp', '6131c898925f06a896a9affa22c3ac73', '大塚　瑠璃', '大塚　瑠璃', 'テストユーザー', 0, 0),
(20, '2201575@s.asojuku.ac.jp', 'dd226b3d11f281c6b34341c507ea8813', '大野　航聖', '大野　航聖', 'テストユーザー', 0, 0),
(21, '2201608@s.asojuku.ac.jp', '9a05ae91389a46e63b698d47aec38f0d', '岡元　駿弥', '岡元　駿弥', 'テストユーザー', 0, 0),
(22, '2201576@s.asojuku.ac.jp', '51e5081ae802d67bc34d6254f74aaf8c', '春日　宏啓', '春日　宏啓', 'テストユーザー', 0, 0),
(23, '2201577@s.asojuku.ac.jp', 'd9260b669d4bbdda11030076538555ba', '川島　愛央', '川島　愛央', 'テストユーザー', 0, 0),
(24, '2201609@s.asojuku.ac.jp', 'f8de88760ee2e0b607b774823bd211ae', '倉富　裕大', '倉富　裕大', 'テストユーザー', 0, 0),
(25, '2201605@s.asojuku.ac.jp', '09fcbb8ba1b4c20d0cef9cd7dd17a062', 'HOU YUTONG', 'HOU YUTONG', 'テストユーザー', 0, 0),
(26, '2201578@s.asojuku.ac.jp', '53b1bec007c81deb09e2122058763dc5', '河渕　和輝', '河渕　和輝', 'テストユーザー', 0, 0),
(27, '2201579@s.asojuku.ac.jp', 'ea8df9fb863a272e95ddf291937cfd41', '兒玉　稜', '兒玉　稜', 'テストユーザー', 0, 0),
(28, '2201580@s.asojuku.ac.jp', '39e4d23d9f5470bab23317a03a414b85', '近藤　沙紀', '近藤　沙紀', 'テストユーザー', 0, 0),
(29, '2201581@s.asojuku.ac.jp', 'c712e983c182d9b7ac8b01952c52dbf0', '酒井　那帆', '酒井　那帆', 'テストユーザー', 0, 0),
(30, '2101163@s.asojuku.ac.jp', 'ab163faf11932e7e00b07bb487abb4e1', '指出　拓紀', '指出　拓紀', 'テストユーザー', 0, 0),
(31, '2201582@s.asojuku.ac.jp', 'a5737d9fdd64df61f8f2544c4fe7ee5a', '佐野　宏樹', '佐野　宏樹', 'テストユーザー', 0, 0),
(32, '2201584@s.asojuku.ac.jp', '00080d695b94924b2c99c4b0305b8aa6', '下道　悠', '下道　悠', 'テストユーザー', 0, 0),
(33, '2201586@s.asojuku.ac.jp', 'bd29925aa8efbf845277ccba6d679d1d', '滝本　光里', '滝本　光里', 'テストユーザー', 0, 0),
(34, '2201587@s.asojuku.ac.jp', '30cd0df996b2f99a2ac9fd85f039892b', '田島　芽衣', '田島　芽衣', 'テストユーザー', 0, 0),
(35, '2201588@s.asojuku.ac.jp', '7840611455a19803486235d4bf3cf0d0', '谷口　修一', '谷口　修一', 'テストユーザー', 0, 0),
(36, '2201589@s.asojuku.ac.jp', 'c57ad4dfaac4e9e709c47e44c3e89e6a', '德重　理沙', '德重　理沙', 'テストユーザー', 0, 0),
(37, '2201590@s.asojuku.ac.jp', '2a0e2821c74e26e7f5c3cd528a9f9edb', '德田　陸', '德田　陸', 'テストユーザー', 0, 0),
(38, '2201591@s.asojuku.ac.jp', 'd4d4d01db2ab6673a0622cc5ee2f7cb0', '砥綿　夕空', '砥綿　夕空', 'テストユーザー', 0, 0),
(39, '2201592@s.asojuku.ac.jp', 'fd7b72ba481f85f55e56e15a24d5a21a', '中上　太陽', '中上　太陽', 'テストユーザー', 0, 0),
(40, '2201593@s.asojuku.ac.jp', 'aabda5d19a23633aea9c3d9a32c29de8', '仲間　修斗', '仲間　修斗', 'テストユーザー', 0, 0),
(41, '2201594@s.asojuku.ac.jp', 'abca2c19e23c5af153abdd8c7cacd994', '中村　友祐', '中村　友祐', 'テストユーザー', 0, 0),
(42, '2201595@s.asojuku.ac.jp', '5cf80d8d165d76bd6a3e08560ae20fcb', '中山　千咲', '中山　千咲', 'テストユーザー', 0, 0),
(43, '2201596@s.asojuku.ac.jp', 'fb7d3ba413095a72a13be9555b04ae12', '藤木　彩愛', '藤木　彩愛', 'テストユーザー', 0, 0),
(44, '2201612@s.asojuku.ac.jp', '8b5a0363f34539ef1f8843ef4a48e3fa', '堀　優弥', '堀　優弥', 'テストユーザー', 0, 0),
(45, '2201597@s.asojuku.ac.jp', 'da5bd2123e0d7f38358a537bc6b37054', '松本　愛加', '松本　愛加', 'テストユーザー', 0, 0),
(46, '2201598@s.asojuku.ac.jp', '899edbb44bebd724ab3be8f0d34c710f', '松本　彪', '松本　彪', 'テストユーザー', 0, 0),
(47, '2201613@s.asojuku.ac.jp', '558aa91996a02a731903be587bdc564d', '南　昌志', '南　昌志', 'テストユーザー', 0, 0),
(48, '2201615@s.asojuku.ac.jp', 'f99b0496d250e8b151c7d0cbc2ce739c', '宮原　涼', '宮原　涼', 'テストユーザー', 0, 0),
(49, '2201599@s.asojuku.ac.jp', '04073e6c244e53dd60958774bd20c1f5', '本山　健太', '本山　健太', 'テストユーザー', 0, 0),
(50, '2201600@s.asojuku.ac.jp', 'ddbbc4529d3d96cbffb47569febff612', '森川　疾斗', '森川　疾斗', 'テストユーザー', 0, 0),
(51, '2201601@s.asojuku.ac.jp', '2fa71e7bcb579b309a20316f191bd922', '森山　美月', '森山　美月', 'テストユーザー', 0, 0),
(52, '2201602@s.asojuku.ac.jp', '8b99e58cfb05efaff5b08e188fc97114', '柳生　朔夜', '柳生　朔夜', 'テストユーザー', 0, 0),
(53, '2201603@s.asojuku.ac.jp', '4a883fd0c63535b8ab58737ea024c91a', '柳本　璃玖', '柳本　璃玖', 'テストユーザー', 0, 0),
(54, '2201604@s.asojuku.ac.jp', '0ab331ff2312235b7570171fb3529dbd', '吉積　蓮', '吉積　蓮', 'テストユーザー', 0, 0);

--
-- ダンプしたテーブルのインデックス
--

--
-- テーブルのインデックス `ans_history_tbl`
--
ALTER TABLE `ans_history_tbl`
  ADD PRIMARY KEY (`anshid`),
  ADD KEY `ansid` (`ansid`);

--
-- テーブルのインデックス `ans_tbl`
--
ALTER TABLE `ans_tbl`
  ADD PRIMARY KEY (`ansid`),
  ADD UNIQUE KEY `UQ_UEQ` (`uid`,`eqid`),
  ADD KEY `eqid` (`eqid`);

--
-- テーブルのインデックス `ans_temp_tbl`
--
ALTER TABLE `ans_temp_tbl`
  ADD PRIMARY KEY (`token`);

--
-- テーブルのインデックス `event_question`
--
ALTER TABLE `event_question`
  ADD PRIMARY KEY (`eqid`),
  ADD UNIQUE KEY `eidqid` (`eid`,`qid`),
  ADD KEY `qid` (`qid`);

--
-- テーブルのインデックス `event_tbl`
--
ALTER TABLE `event_tbl`
  ADD PRIMARY KEY (`eid`);

--
-- テーブルのインデックス `event_user`
--
ALTER TABLE `event_user`
  ADD PRIMARY KEY (`euid`),
  ADD KEY `eid` (`eid`),
  ADD KEY `uid` (`uid`);

--
-- テーブルのインデックス `qestion_tbl`
--
ALTER TABLE `qestion_tbl`
  ADD PRIMARY KEY (`qid`);

--
-- テーブルのインデックス `question_tbl`
--
ALTER TABLE `question_tbl`
  ADD PRIMARY KEY (`qid`);

--
-- テーブルのインデックス `user_tbl`
--
ALTER TABLE `user_tbl`
  ADD PRIMARY KEY (`uid`);

--
-- ダンプしたテーブルの AUTO_INCREMENT
--

--
-- テーブルの AUTO_INCREMENT `ans_history_tbl`
--
ALTER TABLE `ans_history_tbl`
  MODIFY `anshid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- テーブルの AUTO_INCREMENT `ans_tbl`
--
ALTER TABLE `ans_tbl`
  MODIFY `ansid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- テーブルの AUTO_INCREMENT `event_question`
--
ALTER TABLE `event_question`
  MODIFY `eqid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- テーブルの AUTO_INCREMENT `event_tbl`
--
ALTER TABLE `event_tbl`
  MODIFY `eid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- テーブルの AUTO_INCREMENT `event_user`
--
ALTER TABLE `event_user`
  MODIFY `euid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- テーブルの AUTO_INCREMENT `qestion_tbl`
--
ALTER TABLE `qestion_tbl`
  MODIFY `qid` int(11) NOT NULL AUTO_INCREMENT;

--
-- テーブルの AUTO_INCREMENT `question_tbl`
--
ALTER TABLE `question_tbl`
  MODIFY `qid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- テーブルの AUTO_INCREMENT `user_tbl`
--
ALTER TABLE `user_tbl`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- ダンプしたテーブルの制約
--

--
-- テーブルの制約 `ans_history_tbl`
--
ALTER TABLE `ans_history_tbl`
  ADD CONSTRAINT `ans_history_tbl_ibfk_1` FOREIGN KEY (`ansid`) REFERENCES `ans_tbl` (`ansid`);

--
-- テーブルの制約 `ans_tbl`
--
ALTER TABLE `ans_tbl`
  ADD CONSTRAINT `ans_tbl_ibfk_1` FOREIGN KEY (`eqid`) REFERENCES `event_question` (`eqid`),
  ADD CONSTRAINT `ans_tbl_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_tbl` (`uid`);

--
-- テーブルの制約 `event_question`
--
ALTER TABLE `event_question`
  ADD CONSTRAINT `event_question_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `event_tbl` (`eid`),
  ADD CONSTRAINT `event_question_ibfk_2` FOREIGN KEY (`qid`) REFERENCES `question_tbl` (`qid`);

--
-- テーブルの制約 `event_user`
--
ALTER TABLE `event_user`
  ADD CONSTRAINT `event_user_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `event_tbl` (`eid`),
  ADD CONSTRAINT `event_user_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_tbl` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
