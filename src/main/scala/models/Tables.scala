package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Client.schema, Employee.schema, Goods.schema, Payments.schema, Position.schema, Sales.schema, Test.schema, User.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Client
   *  @param idclient Database column idClient SqlType(int8), AutoInc, PrimaryKey
   *  @param iduser Database column idUser SqlType(int8)
   *  @param amount Database column amount SqlType(int8), Default(None) */
  case class ClientRow(idclient: Long, iduser: Long, amount: Option[Long] = None)
  /** GetResult implicit for fetching ClientRow objects using plain SQL queries */
  implicit def GetResultClientRow(implicit e0: GR[Long], e1: GR[Option[Long]]): GR[ClientRow] = GR{
    prs => import prs._
    ClientRow.tupled((<<[Long], <<[Long], <<?[Long]))
  }
  /** Table description of table Client. Objects of this class serve as prototypes for rows in queries. */
  class Client(_tableTag: Tag) extends profile.api.Table[ClientRow](_tableTag, "Client") {
    def * = (idclient, iduser, amount) <> (ClientRow.tupled, ClientRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idclient), Rep.Some(iduser), amount)).shaped.<>({r=>import r._; _1.map(_=> ClientRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idClient SqlType(int8), AutoInc, PrimaryKey */
    val idclient: Rep[Long] = column[Long]("idClient", O.AutoInc, O.PrimaryKey)
    /** Database column idUser SqlType(int8) */
    val iduser: Rep[Long] = column[Long]("idUser")
    /** Database column amount SqlType(int8), Default(None) */
    val amount: Rep[Option[Long]] = column[Option[Long]]("amount", O.Default(None))

    /** Foreign key referencing User (database name Client_idUser_fkey) */
    lazy val userFk = foreignKey("Client_idUser_fkey", iduser, User)(r => r.idUser, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Client */
  lazy val Client = new TableQuery(tag => new Client(tag))

  /** Entity class storing rows of table Employee
   *  @param idemployee Database column idEmployee SqlType(int8), AutoInc, PrimaryKey
   *  @param fullName Database column full_name SqlType(varchar), Length(255,true)
   *  @param passport Database column passport SqlType(text), Default(None)
   *  @param idposition Database column idPosition SqlType(int8)
   *  @param iduser Database column idUser SqlType(int8) */
  case class EmployeeRow(idemployee: Long, fullName: String, passport: Option[String] = None, idposition: Long, iduser: Long)
  /** GetResult implicit for fetching EmployeeRow objects using plain SQL queries */
  implicit def GetResultEmployeeRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[EmployeeRow] = GR{
    prs => import prs._
    EmployeeRow.tupled((<<[Long], <<[String], <<?[String], <<[Long], <<[Long]))
  }
  /** Table description of table Employee. Objects of this class serve as prototypes for rows in queries. */
  class Employee(_tableTag: Tag) extends profile.api.Table[EmployeeRow](_tableTag, "Employee") {
    def * = (idemployee, fullName, passport, idposition, iduser) <> (EmployeeRow.tupled, EmployeeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idemployee), Rep.Some(fullName), passport, Rep.Some(idposition), Rep.Some(iduser))).shaped.<>({r=>import r._; _1.map(_=> EmployeeRow.tupled((_1.get, _2.get, _3, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idEmployee SqlType(int8), AutoInc, PrimaryKey */
    val idemployee: Rep[Long] = column[Long]("idEmployee", O.AutoInc, O.PrimaryKey)
    /** Database column full_name SqlType(varchar), Length(255,true) */
    val fullName: Rep[String] = column[String]("full_name", O.Length(255,varying=true))
    /** Database column passport SqlType(text), Default(None) */
    val passport: Rep[Option[String]] = column[Option[String]]("passport", O.Default(None))
    /** Database column idPosition SqlType(int8) */
    val idposition: Rep[Long] = column[Long]("idPosition")
    /** Database column idUser SqlType(int8) */
    val iduser: Rep[Long] = column[Long]("idUser")

    /** Foreign key referencing Position (database name Employee_idPosition_fkey) */
    lazy val positionFk = foreignKey("Employee_idPosition_fkey", idposition, Position)(r => r.idposition, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name Employee_idUser_fkey) */
    lazy val userFk = foreignKey("Employee_idUser_fkey", iduser, User)(r => r.idUser, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Employee */
  lazy val Employee = new TableQuery(tag => new Employee(tag))

  /** Entity class storing rows of table Goods
   *  @param idgoods Database column idGoods SqlType(int8), AutoInc, PrimaryKey
   *  @param namegoods Database column nameGoods SqlType(varchar), Length(255,true)
   *  @param characteristics Database column characteristics SqlType(text)
   *  @param note Database column note SqlType(text), Default(None) */
  case class GoodsRow(idgoods: Long, namegoods: String, characteristics: String, note: Option[String] = None)
  /** GetResult implicit for fetching GoodsRow objects using plain SQL queries */
  implicit def GetResultGoodsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[GoodsRow] = GR{
    prs => import prs._
    GoodsRow.tupled((<<[Long], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table Goods. Objects of this class serve as prototypes for rows in queries. */
  class Goods(_tableTag: Tag) extends profile.api.Table[GoodsRow](_tableTag, "Goods") {
    def * = (idgoods, namegoods, characteristics, note) <> (GoodsRow.tupled, GoodsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idgoods), Rep.Some(namegoods), Rep.Some(characteristics), note)).shaped.<>({r=>import r._; _1.map(_=> GoodsRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idGoods SqlType(int8), AutoInc, PrimaryKey */
    val idgoods: Rep[Long] = column[Long]("idGoods", O.AutoInc, O.PrimaryKey)
    /** Database column nameGoods SqlType(varchar), Length(255,true) */
    val namegoods: Rep[String] = column[String]("nameGoods", O.Length(255,varying=true))
    /** Database column characteristics SqlType(text) */
    val characteristics: Rep[String] = column[String]("characteristics")
    /** Database column note SqlType(text), Default(None) */
    val note: Rep[Option[String]] = column[Option[String]]("note", O.Default(None))
  }
  /** Collection-like TableQuery object for table Goods */
  lazy val Goods = new TableQuery(tag => new Goods(tag))

  /** Entity class storing rows of table Payments
   *  @param idpayments Database column idPayments SqlType(int8), AutoInc, PrimaryKey
   *  @param paymentdate Database column paymentDate SqlType(date), Default(None)
   *  @param ordercompletionmark Database column orderCompletionMark SqlType(int2), Default(None)
   *  @param paymentamount Database column paymentAmount SqlType(int8), Default(None) */
  case class PaymentsRow(idpayments: Long, paymentdate: Option[java.sql.Date] = None, ordercompletionmark: Option[Short] = None, paymentamount: Option[Long] = None)
  /** GetResult implicit for fetching PaymentsRow objects using plain SQL queries */
  implicit def GetResultPaymentsRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Date]], e2: GR[Option[Short]], e3: GR[Option[Long]]): GR[PaymentsRow] = GR{
    prs => import prs._
    PaymentsRow.tupled((<<[Long], <<?[java.sql.Date], <<?[Short], <<?[Long]))
  }
  /** Table description of table Payments. Objects of this class serve as prototypes for rows in queries. */
  class Payments(_tableTag: Tag) extends profile.api.Table[PaymentsRow](_tableTag, "Payments") {
    def * = (idpayments, paymentdate, ordercompletionmark, paymentamount) <> (PaymentsRow.tupled, PaymentsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idpayments), paymentdate, ordercompletionmark, paymentamount)).shaped.<>({r=>import r._; _1.map(_=> PaymentsRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idPayments SqlType(int8), AutoInc, PrimaryKey */
    val idpayments: Rep[Long] = column[Long]("idPayments", O.AutoInc, O.PrimaryKey)
    /** Database column paymentDate SqlType(date), Default(None) */
    val paymentdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("paymentDate", O.Default(None))
    /** Database column orderCompletionMark SqlType(int2), Default(None) */
    val ordercompletionmark: Rep[Option[Short]] = column[Option[Short]]("orderCompletionMark", O.Default(None))
    /** Database column paymentAmount SqlType(int8), Default(None) */
    val paymentamount: Rep[Option[Long]] = column[Option[Long]]("paymentAmount", O.Default(None))
  }
  /** Collection-like TableQuery object for table Payments */
  lazy val Payments = new TableQuery(tag => new Payments(tag))

  /** Entity class storing rows of table Position
   *  @param idposition Database column idPosition SqlType(int8), AutoInc, PrimaryKey
   *  @param nameposition Database column namePosition SqlType(varchar), Length(255,true)
   *  @param salary Database column salary SqlType(int8), Default(None)
   *  @param note Database column note SqlType(text), Default(None) */
  case class PositionRow(idposition: Long, nameposition: String, salary: Option[Long] = None, note: Option[String] = None)
  /** GetResult implicit for fetching PositionRow objects using plain SQL queries */
  implicit def GetResultPositionRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]], e3: GR[Option[String]]): GR[PositionRow] = GR{
    prs => import prs._
    PositionRow.tupled((<<[Long], <<[String], <<?[Long], <<?[String]))
  }
  /** Table description of table Position. Objects of this class serve as prototypes for rows in queries. */
  class Position(_tableTag: Tag) extends profile.api.Table[PositionRow](_tableTag, "Position") {
    def * = (idposition, nameposition, salary, note) <> (PositionRow.tupled, PositionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idposition), Rep.Some(nameposition), salary, note)).shaped.<>({r=>import r._; _1.map(_=> PositionRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idPosition SqlType(int8), AutoInc, PrimaryKey */
    val idposition: Rep[Long] = column[Long]("idPosition", O.AutoInc, O.PrimaryKey)
    /** Database column namePosition SqlType(varchar), Length(255,true) */
    val nameposition: Rep[String] = column[String]("namePosition", O.Length(255,varying=true))
    /** Database column salary SqlType(int8), Default(None) */
    val salary: Rep[Option[Long]] = column[Option[Long]]("salary", O.Default(None))
    /** Database column note SqlType(text), Default(None) */
    val note: Rep[Option[String]] = column[Option[String]]("note", O.Default(None))
  }
  /** Collection-like TableQuery object for table Position */
  lazy val Position = new TableQuery(tag => new Position(tag))

  /** Entity class storing rows of table Sales
   *  @param idsaller Database column idSaller SqlType(int8)
   *  @param idclient Database column idClient SqlType(int8)
   *  @param idpayments Database column idPayments SqlType(int8)
   *  @param idgoods Database column idGoods SqlType(int8) */
  case class SalesRow(idsaller: Long, idclient: Long, idpayments: Long, idgoods: Long)
  /** GetResult implicit for fetching SalesRow objects using plain SQL queries */
  implicit def GetResultSalesRow(implicit e0: GR[Long]): GR[SalesRow] = GR{
    prs => import prs._
    SalesRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table Sales. Objects of this class serve as prototypes for rows in queries. */
  class Sales(_tableTag: Tag) extends profile.api.Table[SalesRow](_tableTag, "Sales") {
    def * = (idsaller, idclient, idpayments, idgoods) <> (SalesRow.tupled, SalesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idsaller), Rep.Some(idclient), Rep.Some(idpayments), Rep.Some(idgoods))).shaped.<>({r=>import r._; _1.map(_=> SalesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column idSaller SqlType(int8) */
    val idsaller: Rep[Long] = column[Long]("idSaller")
    /** Database column idClient SqlType(int8) */
    val idclient: Rep[Long] = column[Long]("idClient")
    /** Database column idPayments SqlType(int8) */
    val idpayments: Rep[Long] = column[Long]("idPayments")
    /** Database column idGoods SqlType(int8) */
    val idgoods: Rep[Long] = column[Long]("idGoods")

    /** Foreign key referencing Client (database name Sales_idClient_fkey) */
    lazy val clientFk = foreignKey("Sales_idClient_fkey", idclient, Client)(r => r.idclient, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Employee (database name Sales_idSaller_fkey) */
    lazy val employeeFk = foreignKey("Sales_idSaller_fkey", idsaller, Employee)(r => r.idemployee, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Goods (database name Sales_idGoods_fkey) */
    lazy val goodsFk = foreignKey("Sales_idGoods_fkey", idgoods, Goods)(r => r.idgoods, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Payments (database name Sales_idPayments_fkey) */
    lazy val paymentsFk = foreignKey("Sales_idPayments_fkey", idpayments, Payments)(r => r.idpayments, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Sales */
  lazy val Sales = new TableQuery(tag => new Sales(tag))

  /** Entity class storing rows of table Test
   *  @param test Database column test SqlType(int8), Default(None) */
  case class TestRow(test: Option[Long] = None)
  /** GetResult implicit for fetching TestRow objects using plain SQL queries */
  implicit def GetResultTestRow(implicit e0: GR[Option[Long]]): GR[TestRow] = GR{
    prs => import prs._
    TestRow(<<?[Long])
  }
  /** Table description of table test. Objects of this class serve as prototypes for rows in queries. */
  class Test(_tableTag: Tag) extends profile.api.Table[TestRow](_tableTag, "test") {
    def * = test <> (TestRow, TestRow.unapply)

    /** Database column test SqlType(int8), Default(None) */
    val test: Rep[Option[Long]] = column[Option[Long]]("test", O.Default(None))
  }
  /** Collection-like TableQuery object for table Test */
  lazy val Test = new TableQuery(tag => new Test(tag))

  /** Entity class storing rows of table User
   *  @param idUser Database column id_user SqlType(int8), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(varchar), Length(255,true)
   *  @param password Database column password SqlType(varchar), Length(255,true) */
  case class UserRow(idUser: Long, login: String, password: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Long], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, "user") {
    def * = (idUser, login, password) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(idUser), Rep.Some(login), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id_user SqlType(int8), AutoInc, PrimaryKey */
    val idUser: Rep[Long] = column[Long]("id_user", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(varchar), Length(255,true) */
    val login: Rep[String] = column[String]("login", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
