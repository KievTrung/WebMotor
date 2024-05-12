USE [master]
GO
/****** Object:  Database [WebMotor]    Script Date: 5/12/2024 8:49:50 PM ******/
CREATE DATABASE [WebMotor]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'WebMotor', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\WebMotor.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'WebMotor_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\WebMotor_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [WebMotor] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [WebMotor].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [WebMotor] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [WebMotor] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [WebMotor] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [WebMotor] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [WebMotor] SET ARITHABORT OFF 
GO
ALTER DATABASE [WebMotor] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [WebMotor] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [WebMotor] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [WebMotor] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [WebMotor] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [WebMotor] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [WebMotor] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [WebMotor] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [WebMotor] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [WebMotor] SET  DISABLE_BROKER 
GO
ALTER DATABASE [WebMotor] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [WebMotor] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [WebMotor] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [WebMotor] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [WebMotor] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [WebMotor] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [WebMotor] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [WebMotor] SET RECOVERY FULL 
GO
ALTER DATABASE [WebMotor] SET  MULTI_USER 
GO
ALTER DATABASE [WebMotor] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [WebMotor] SET DB_CHAINING OFF 
GO
ALTER DATABASE [WebMotor] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [WebMotor] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [WebMotor] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [WebMotor] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'WebMotor', N'ON'
GO
ALTER DATABASE [WebMotor] SET QUERY_STORE = ON
GO
ALTER DATABASE [WebMotor] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [WebMotor]
GO
/****** Object:  Table [dbo].[account]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[account](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[login_name] [nvarchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [int] NULL,
	[email] [varchar](255) NULL,
	[diaChi] [nvarchar](255) NULL,
	[isAdmin] [bit] NOT NULL,
	[isActive] [bit] NOT NULL,
 CONSTRAINT [pk_account] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [uq_account] UNIQUE NONCLUSTERED 
(
	[login_name] ASC,
	[phone] ASC,
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[chiTietDonHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[chiTietDonHang](
	[soHoaDon] [int] NOT NULL,
	[maXe] [varchar](10) NOT NULL,
	[soLuong] [int] NULL,
 CONSTRAINT [pk_chiTietDonHang] PRIMARY KEY CLUSTERED 
(
	[soHoaDon] ASC,
	[maXe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[chiTietXe]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[chiTietXe](
	[maXe] [varchar](10) NOT NULL,
	[tenXe] [nvarchar](255) NOT NULL,
	[giaXe] [int] NOT NULL,
	[loaiXe] [nvarchar](255) NOT NULL,
	[description] [nvarchar](max) NOT NULL,
	[soLuongTonKho] [int] NOT NULL,
	[indexMode] [tinyint] NULL,
 CONSTRAINT [pk_xe] PRIMARY KEY CLUSTERED 
(
	[maXe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[tenXe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[donHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[donHang](
	[soHoaDon] [int] IDENTITY(1,1) NOT NULL,
	[ngayTao] [date] NULL,
	[id] [int] NULL,
	[HinhThucThanhToan] [nvarchar](255) NULL,
	[trangThaiThanhToan] [bit] NULL,
	[additionInfo] [varchar](255) NULL,
	[diaChi] [nvarchar](255) NULL,
 CONSTRAINT [pk_hoaDon] PRIMARY KEY CLUSTERED 
(
	[soHoaDon] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[gioHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[gioHang](
	[id] [int] NOT NULL,
	[maXe] [varchar](10) NOT NULL,
	[soLuong] [int] NOT NULL,
 CONSTRAINT [pk_gioHang] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[maXe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[vehiclePictures]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[vehiclePictures](
	[maXe] [varchar](10) NOT NULL,
	[ten] [varchar](255) NOT NULL,
 CONSTRAINT [pk_vehiclePictures] PRIMARY KEY CLUSTERED 
(
	[maXe] ASC,
	[ten] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[chiTietDonHang]  WITH CHECK ADD  CONSTRAINT [fk_chiTietDonHang_chiTietXe] FOREIGN KEY([maXe])
REFERENCES [dbo].[chiTietXe] ([maXe])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[chiTietDonHang] CHECK CONSTRAINT [fk_chiTietDonHang_chiTietXe]
GO
ALTER TABLE [dbo].[chiTietDonHang]  WITH CHECK ADD  CONSTRAINT [fk_chiTietDonHang_donHang] FOREIGN KEY([soHoaDon])
REFERENCES [dbo].[donHang] ([soHoaDon])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[chiTietDonHang] CHECK CONSTRAINT [fk_chiTietDonHang_donHang]
GO
ALTER TABLE [dbo].[donHang]  WITH CHECK ADD  CONSTRAINT [fk_donHang_account] FOREIGN KEY([id])
REFERENCES [dbo].[account] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[donHang] CHECK CONSTRAINT [fk_donHang_account]
GO
ALTER TABLE [dbo].[gioHang]  WITH CHECK ADD  CONSTRAINT [fk_gioHang_account] FOREIGN KEY([id])
REFERENCES [dbo].[account] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[gioHang] CHECK CONSTRAINT [fk_gioHang_account]
GO
ALTER TABLE [dbo].[gioHang]  WITH CHECK ADD  CONSTRAINT [fk_gioHang_chiTietXe] FOREIGN KEY([maXe])
REFERENCES [dbo].[chiTietXe] ([maXe])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[gioHang] CHECK CONSTRAINT [fk_gioHang_chiTietXe]
GO
ALTER TABLE [dbo].[vehiclePictures]  WITH CHECK ADD  CONSTRAINT [fk_vehiclePictures_chiTietXe] FOREIGN KEY([maXe])
REFERENCES [dbo].[chiTietXe] ([maXe])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[vehiclePictures] CHECK CONSTRAINT [fk_vehiclePictures_chiTietXe]
GO
ALTER TABLE [dbo].[account]  WITH CHECK ADD CHECK  (([phone]>=(0)))
GO
ALTER TABLE [dbo].[chiTietXe]  WITH CHECK ADD CHECK  (([giaXe]>=(0)))
GO
ALTER TABLE [dbo].[chiTietXe]  WITH CHECK ADD CHECK  (([soLuongTonKho]>=(0)))
GO
ALTER TABLE [dbo].[gioHang]  WITH CHECK ADD CHECK  (([soLuong]>(0)))
GO
/****** Object:  StoredProcedure [dbo].[sp_test]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
	CREATE proc [dbo].[sp_test]
		@soHoaDon int
	as
		declare @maXe varchar(10), 
			@tenXe nvarchar(255), 
			@soLuongYeuCau int, 
			@soLuongTonKho int,  
			@msg varchar(max);
	--check stock
	declare recordCursor cursor for select maXe, soLuong from chiTietDonHang where soHoaDon = @soHoaDon
	open recordCursor
	fetch next from recordCursor into @maXe, @soLuongYeuCau
	begin try
		begin tran
			while @@FETCH_STATUS = 0
			begin
				select @tenXe = tenXe, @soLuongTonKho = soLuongTonKho from dbo.chiTietXe where maXe = @maXe
				if @soLuongYeuCau > @soLuongTonKho
					raiserror('hello', 16, 13)
				update dbo.chiTietXe
				set soLuongTonKho = soLuongTonKho - @soLuongYeuCau
				where maXe = @maXe
				fetch next from recordCursor into @maXe, @soLuongYeuCau
			end
			close recordCursor
		commit tran
	end try
	begin catch
		rollback tran
		close recordCursor
		raiserror('hello', 16, 13)
	end catch
	deallocate recordCursor
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_addVehiclePicture]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[sp_tr_addVehiclePicture]
	@maXe varchar(10),
	@name varchar(255),
	@result int = -1 output
as
	if exists(select maXe from dbo.chiTietXe where maXe = @maXe)
	begin
		insert into dbo.vehiclePictures values (@maXe, @name)
		set @result = 1
	end
	else
		raiserror(N'Mã xe không tồn tại', 16, 7)
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_changeLoginName]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_changeLoginName] 
	@id int,
	@newLogin nvarchar(255),
	@result int = -1 output
as
	if exists(select id from dbo.account where login_name = @newLogin)
	begin
		raiserror('Login này đã tồn tại', 16, 9)
		return
	end

	update dbo.account
	set login_name = @newLogin
	where id = @id
	set @result = 1
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_getCategoryPicture]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_getCategoryPicture]
as
	select ctx.loaiXe, vp.ten
	from dbo.chiTietXe ctx inner join dbo.vehiclePictures vp on ctx.maXe = vp.maXe
	where vp.ten like '%_0.%' and GET_BIT(indexMode, 0) = 1
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_getFeatureProduct]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_getFeatureProduct]
as
	select ctx.tenXe, ctx.giaXe, vp.ten, ctx.maXe
	from dbo.chiTietXe ctx inner join dbo.vehiclePictures vp on ctx.maXe = vp.maXe
	where vp.ten like '%_0.%' and GET_BIT(indexMode, 1) = 1
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_getIndexProduct]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_getIndexProduct]
as
	select ctx.tenXe, ctx.description, vp.ten, ctx.maXe
	from dbo.chiTietXe ctx inner join dbo.vehiclePictures vp on ctx.maXe = vp.maXe
	where GET_BIT(indexMode, 3) = 1 and vp.ten like '%_.%'
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_getSpecialProduct]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_getSpecialProduct]
as
	select ctx.tenXe, ctx.description, vp.ten, ctx.maXe
	from dbo.chiTietXe ctx inner join dbo.vehiclePictures vp on ctx.maXe = vp.maXe
	where vp.ten like '%_0.%' and GET_BIT(indexMode, 2) = 1
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_restoreDonHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_restoreDonHang]
		@soHoaDon int
	as
	declare @maXe varchar(10), @soLuong int, @msg varchar(max)
	declare @userId int = (select id from dbo.donHang where soHoaDon = @soHoaDon)
	
	declare recordCursor cursor for select maXe, soLuong from dbo.chiTietDonHang  where soHoaDon = @soHoaDon
	open recordCursor
	fetch next from recordCursor into @maXe, @soLuong
	begin try
		begin tran
			while @@FETCH_STATUS = 0
			begin
				if exists(select maXe from dbo.gioHang where id = @userId and maXe = @maXe)
				begin
					update dbo.gioHang
					set soLuong = (select soLuongTonKho from dbo.chiTietXe where maXe = @maXe)
					where id = @userId and maXe = @maXe 
				end
				else
					insert into dbo.gioHang values (@userId, @maXe, @soLuong)
				fetch next from recordCursor into @maXe, @soLuong
			end
		close recordCursor
		commit tran
	end try
	begin catch
		rollback tran
		close recordCursor
		set @msg = ERROR_MESSAGE()
		raiserror(@msg, 16, 14)
	end catch
	deallocate recordCursor
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_setUpTaiKhoan]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_setUpTaiKhoan]
	@loginName nvarchar(255),
	@password varchar(255),
	@phone int,
	@email varchar(255),
	@diaChi nvarchar(255),
	@isAdmin bit,
	@isActive bit,
	@flag tinyint,
	@result int = -1 output
as
	declare @id int = (select id from dbo.account where login_name = @loginName)
	if @id is not null
	begin
		declare @passFlag bit = get_bit(@flag, 0), 
				@phoneFlag bit = get_bit(@flag, 1), 
				@emailFlag bit = get_bit(@flag, 2),
				@diaChiFlag bit = get_bit(@flag, 3),
				@adminFlag bit = get_bit(@flag, 4),
				@activeFlag bit = get_bit(@flag, 5)
		begin try
			begin tran
				if @passFlag = 1
					update dbo.account set password = @password where id = @id
				if @phoneFlag = 1
					update dbo.account set phone = @phone where id = @id
				if @emailFlag = 1
					update dbo.account set email = @email where id = @id
				if @diaChiFlag = 1
					update dbo.account set diaChi = @diaChi where id = @id
				if @adminFlag = 1
					update dbo.account set isAdmin = @isAdmin where id = @id
				if @activeFlag = 1
					update dbo.account set isActive = @isActive where id = @id
			commit tran
			set @result = 1
			return
		end try
		begin catch
			rollback tran
			declare @msg varchar(max) = ERROR_MESSAGE()
			raiserror(@msg, 16, 6)
		end catch
	end
	else
	begin
		insert into dbo.account (login_name, password, phone, email, diaChi, isAdmin, isActive)
		values (@loginName, @password, @phone, @email, @diaChi, @isAdmin, @isActive)
		set @result = SCOPE_IDENTITY()
	end
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_setUpXe]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_setUpXe]
	@maXe varchar(10),
	@tenXe nvarchar(255),
	@giaXe int,
	@loaiXe nvarchar(255),
	@descrip nvarchar(max),
	@soLuongTonKho int,
	@indexMode tinyint,
	@flag tinyint,
	@result int = -1 output 
as
	declare	@tenFlag bit = get_bit(@flag, 0), 
			@giaFlag bit = get_bit(@flag, 1), 
			@loaiFlag bit = get_bit(@flag, 2),
			@descripFlag bit = get_bit(@flag, 3),
			@sltkFlag bit = get_bit(@flag, 4),
			@indexModeFlag bit = get_bit(@flag, 5)
	if exists(select maXe from dbo.chiTietXe where maXe = @maXe)
	begin
		begin try
			begin tran
				if @tenFlag = 1
					update dbo.chiTietXe set tenXe = @tenXe where maXe = @maXe
				if @giaFlag = 1
					update dbo.chiTietXe set giaXe = @giaXe where maXe = @maXe
				if @loaiFlag = 1
					update dbo.chiTietXe set loaiXe = @loaiXe where maXe = @maXe
				if @descripFlag = 1
					update dbo.chiTietXe set description = @descrip where maXe = @maXe
				if @sltkFlag = 1
					update dbo.chiTietXe set soLuongTonKho = @soLuongTonKho where maXe = @maXe
				if @indexModeFlag = 1
					update dbo.chiTietXe set indexMode = @indexMode where maXe = @maXe
			commit tran
			set @result = 1
			return 
		end try
		begin catch
			rollback tran
			declare @msg varchar(max) = ERROR_MESSAGE()
			raiserror(@msg, 16, 5)
		end catch
	end
	else
	begin
		insert into dbo.chiTietXe (maXe, tenXe, giaXe, loaiXe, description, soLuongTonKho, indexMode)
		values (@maXe, @tenXe, @giaXe, @loaiXe, @descrip, @soLuongTonKho, @indexMode)
		set @result = SCOPE_IDENTITY()
	end
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_taoDonHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_taoDonHang]
	@id int,
	@hinhThucThanhToan nvarchar(255),
	@result int = -1 output
as
	--check xem user id có tồn tại trong db hay ko
	if(not exists(select id from dbo.account where id = @id)) 
	begin
		raiserror(N'user id không tồn tại', 16, 5)
		return
	end
	if(not exists(select id from dbo.gioHang where id = @id))
	begin
		raiserror(N'không có gì trong giỏ hàng', 16, 4)
		return
	end
	--lấy ngày hiện tại 
	declare @date date
	set @date = getdate()
	--lưu vào bảng tạm temp các xe do user chọn và số lượng xe mua
	select maXe, soLuong into #temp from dbo.gioHang where id = @id
	
	declare @maXe varchar(10)
	declare @soLuong int
	declare @identity int
	begin try
		begin tran
			--tạo đơn hàng mới
			insert into dbo.donHang (ngayTao, id, HinhThucThanhToan, trangThaiThanhToan)
			values (@date, @id, @hinhThucThanhToan, 0)
			set @identity = SCOPE_IDENTITY()
			--lưu các thông tin xe, số lượng do user đã chọn vào đơn hàng
			declare recordCursor cursor for select * from #temp
			open recordCursor
			fetch next from recordCursor into @maXe, @soLuong
			while @@FETCH_STATUS = 0
			begin
				insert into dbo.chiTietDonHang (soHoaDon, maXe, soLuong)
				values (@identity, @maXe, @soLuong)
				fetch next from recordCursor into @maXe, @soLuong
			end
			close recordCursor
		commit tran
		set @result = @identity
	end try
	begin catch
		rollback tran
		close recordCursor
		declare @msg varchar(max) = ERROR_MESSAGE()
		raiserror(@msg, 16, 3)
	end catch
	deallocate recordCursor
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_thayDoiSoLuongItemTrongGioHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[sp_tr_thayDoiSoLuongItemTrongGioHang]
	@id int,
	@maXe varchar(10),
	@soLuongYeuCau int,
	@result int = -1 output
as
	declare @soLuongTonKho int = (select soLuongTonKho from dbo.chiTietXe where maXe = @maXe)
	if @soLuongYeuCau > @soLuongTonKho
	begin
		raiserror(N'Số lượng đặt vượt quá số lượng tồn kho', 16, 8)
		return
	end
	update dbo.gioHang
	set soLuong = @soLuongYeuCau
	where id = @id and maXe = @maXe
	set @result = 1
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_themGioHang]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_themGioHang]
	@id int,
	@maXe varchar(10),
	@soLuongYeuCau int,
	@result int = -1 output
as
	if(exists(select id from dbo.account where id = @id) and 
		exists(select maXe from dbo.chiTietXe where maXe = @maXe))
	begin
		declare @soLuongTonKho int = (select soLuongTonKho from dbo.chiTietXe where maXe = @maXe)
		declare @soLuongHienTai int = (select soLuong from dbo.gioHang where id = @id and maXe = @maXe)
		if @soLuongHienTai is null
			set @soLuongHienTai = 0
		if @soLuongHienTai + @soLuongYeuCau > @soLuongTonKho
		begin
			raiserror(N'Số lượng đặt vượt quá số lượng tồn kho', 16, 8)
			return
		end
		if(exists(select id from dbo.gioHang where id = @id and maXe = @maXe))
		begin
			update dbo.gioHang
			set soLuong = soLuong + @soLuongYeuCau
			where id = @id and maXe = @maXe
		end
		else
			insert into dbo.gioHang (id, maXe, soLuong)
			values (@id, @maXe, @soLuongYeuCau)
		set @result = 1
	end
	else 
		raiserror(N'không tồn tại user id hoặc mã xe', 16, 2)
GO
/****** Object:  StoredProcedure [dbo].[sp_tr_updateStock]    Script Date: 5/12/2024 8:49:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_tr_updateStock]
	@soHoaDon int
as
	declare @maXe varchar(10), 
			@tenXe nvarchar(255), 
			@soLuongYeuCau int, 
			@soLuongTonKho int,  
			@msg varchar(max);
	--check stock
	declare recordCursor cursor for select maXe, soLuong from chiTietDonHang where soHoaDon = @soHoaDon
	open recordCursor
	fetch next from recordCursor into @maXe, @soLuongYeuCau
	begin try
		begin tran
			while @@FETCH_STATUS = 0
			begin
				select @tenXe = tenXe, @soLuongTonKho = soLuongTonKho from dbo.chiTietXe where maXe = @maXe
				if @soLuongYeuCau > @soLuongTonKho
					raiserror('', 17, 0)
				update dbo.chiTietXe
				set soLuongTonKho = soLuongTonKho - @soLuongYeuCau
				where maXe = @maXe
				fetch next from recordCursor into @maXe, @soLuongYeuCau
			end
			close recordCursor
		commit tran
	end try
	begin catch
		rollback tran
		close recordCursor
		set @msg = 'Not enough ' + @tenXe + ' to supply, left in stock: ' + convert(varchar, @soLuongTonKho)
		raiserror(@msg, 16, 13)
	end catch
	deallocate recordCursor
GO
insert into dbo.chiTietXe values ('tg0', 'GRANDE', 51546, 'scooter', N'Rất trẻ trung và năng động', 10, 1)
insert into dbo.chiTietXe values ('tg1', 'FREEGO', 34265, 'scooter', N'Tự tin bứt phá mọi cung đường', 10, 2)
insert into dbo.chiTietXe values ('tg2', 'LATTE', 38782, 'scooter', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('tg3', 'NVX', 55300, 'scooter', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('tg4', 'JANUS', 32891, 'scooter', N'Tự tin bứt phá mọi cung đường', 10, null)

insert into dbo.chiTietXe values ('xs0', 'PG-1', 30437, 'Manual', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xs1', 'JUPITER FINN', 28178, 'Manual', N'Tự tin bứt phá mọi cung đường', 10, 2)
insert into dbo.chiTietXe values ('xs2', 'JUPITER FI', 30240, 'Manual', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xs3', 'SIRIUS', 19048, 'Manual', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xs4', 'SIRIUS FI', 21208, 'Manual', N'Tự tin bứt phá mọi cung đường', 10, null)

insert into dbo.chiTietXe values ('xct0', 'EXCITER', 54000, 'Clutch', N'Tự tin bứt phá mọi cung đường', 10, 3)
insert into dbo.chiTietXe values ('xct1', 'WINNER X 2024', 46160, 'Clutch', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xct2', 'CBR150R', 72290, 'Clutch', N'Tự tin bứt phá mọi cung đường', 10, 1)
insert into dbo.chiTietXe values ('xct3', 'XS155R', 77000, 'Clutch', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xct4', 'MT-15', 69000, 'Clutch', N'Tự tin bứt phá mọi cung đường', 10, null)

insert into dbo.chiTietXe values ('xtt0', 'MT-03', 129000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xtt1', 'MT-07', 224000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xtt2', 'MT-09', 259000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, 1)
insert into dbo.chiTietXe values ('xtt3', 'YZF-R3', 132000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xtt4', 'YZF-R7', 239000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, 2)
insert into dbo.chiTietXe values ('xtt5', 'YZF-R15M', 87000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, null)
insert into dbo.chiTietXe values ('xtt6', 'R1M', 468000, 'Sport', N'Tự tin bứt phá mọi cung đường', 10, 8)

insert into dbo.vehiclePictures values ('tg0', 'grande_0.png')
insert into dbo.vehiclePictures values ('tg0', 'grande_1.png')
insert into dbo.vehiclePictures values ('tg0', 'grande_2.png')

insert into dbo.vehiclePictures values ('tg1', 'freego_0.png')
insert into dbo.vehiclePictures values ('tg1', 'freego_1.png')
insert into dbo.vehiclePictures values ('tg1', 'freego_2.png')

insert into dbo.vehiclePictures values ('tg2', 'latte_0.png')
insert into dbo.vehiclePictures values ('tg2', 'latte_1.png')
insert into dbo.vehiclePictures values ('tg2', 'latte_2.png')

insert into dbo.vehiclePictures values ('tg3', 'nvx_0.png')
insert into dbo.vehiclePictures values ('tg3', 'nvx_1.png')
insert into dbo.vehiclePictures values ('tg3', 'nvx_2.png')

insert into dbo.vehiclePictures values ('tg4', 'janus_0.png')
insert into dbo.vehiclePictures values ('tg4', 'janus_1.png')
insert into dbo.vehiclePictures values ('tg4', 'janus_2.png')
											   
insert into dbo.vehiclePictures values ('xs0', 'pg_0.png')
insert into dbo.vehiclePictures values ('xs0', 'pg_1.png')
insert into dbo.vehiclePictures values ('xs0', 'pg_2.png')

insert into dbo.vehiclePictures values ('xs1', 'jupiterfinn_0.png')
insert into dbo.vehiclePictures values ('xs1', 'jupiterfinn_1.png')
insert into dbo.vehiclePictures values ('xs1', 'jupiterfinn_2.png')

insert into dbo.vehiclePictures values ('xs2', 'jupiterfi_0.png')
insert into dbo.vehiclePictures values ('xs2', 'jupiterfi_1.png')
insert into dbo.vehiclePictures values ('xs2', 'jupiterfi_2.png')

insert into dbo.vehiclePictures values ('xs3', 'sirius_0.png')
insert into dbo.vehiclePictures values ('xs3', 'sirius_1.png')
insert into dbo.vehiclePictures values ('xs3', 'sirius_2.png')

insert into dbo.vehiclePictures values ('xs4', 'siriusfi_0.png')
insert into dbo.vehiclePictures values ('xs4', 'siriusfi_1.png')
insert into dbo.vehiclePictures values ('xs4', 'siriusfi_2.png')
											   
insert into dbo.vehiclePictures values ('xct0', 'exiter_0.png')
insert into dbo.vehiclePictures values ('xct0', 'exiter_1.png')
insert into dbo.vehiclePictures values ('xct0', 'exiter_2.png')

insert into dbo.vehiclePictures values ('xct1', 'winner_0.jpg')
insert into dbo.vehiclePictures values ('xct1', 'winner_1.jpg')
insert into dbo.vehiclePictures values ('xct1', 'winner_2.jpg')

insert into dbo.vehiclePictures values ('xct2', 'cbr150r_0.png')
insert into dbo.vehiclePictures values ('xct2', 'cbr150r_1.png')
insert into dbo.vehiclePictures values ('xct2', 'cbr150r_2.jpg')

insert into dbo.vehiclePictures values ('xct3', 'xs155_0.png')
insert into dbo.vehiclePictures values ('xct3', 'xs155_1.png')
insert into dbo.vehiclePictures values ('xct3', 'xs155_2.png')

insert into dbo.vehiclePictures values ('xct4', 'mt15_0.png')
insert into dbo.vehiclePictures values ('xct4', 'mt15_1.jpg')
insert into dbo.vehiclePictures values ('xct4', 'mt15_2.png')
											   
insert into dbo.vehiclePictures values ('xtt0', 'mt3_0.png')
insert into dbo.vehiclePictures values ('xtt0', 'mt3_1.png')
insert into dbo.vehiclePictures values ('xtt0', 'mt3_2.png')

insert into dbo.vehiclePictures values ('xtt1', 'mt7_0.png')
insert into dbo.vehiclePictures values ('xtt1', 'mt7_1.png')
insert into dbo.vehiclePictures values ('xtt1', 'mt7_2.png')
insert into dbo.vehiclePictures values ('xtt1', 'mt7_3.png')

insert into dbo.vehiclePictures values ('xtt2', 'mt9_0.png')
insert into dbo.vehiclePictures values ('xtt2', 'mt9_1.png')
insert into dbo.vehiclePictures values ('xtt2', 'mt9_2.png')
insert into dbo.vehiclePictures values ('xtt2', 'mt9_3.png')

insert into dbo.vehiclePictures values ('xtt3', 'r3_0.png')
insert into dbo.vehiclePictures values ('xtt3', 'r3_1.png')
insert into dbo.vehiclePictures values ('xtt3', 'r3_2.png')

insert into dbo.vehiclePictures values ('xtt4', 'r7_0.png')
insert into dbo.vehiclePictures values ('xtt4', 'r7_1.png')
insert into dbo.vehiclePictures values ('xtt4', 'r7_2.png')
insert into dbo.vehiclePictures values ('xtt4', 'r7_3.png')

insert into dbo.vehiclePictures values ('xtt5', 'r15_0.png')
insert into dbo.vehiclePictures values ('xtt5', 'r15_1.png')
insert into dbo.vehiclePictures values ('xtt5', 'r15_2.png')

insert into dbo.vehiclePictures values ('xtt6', 'r1m_.jpg')
insert into dbo.vehiclePictures values ('xtt6', 'r1m_0.png')
insert into dbo.vehiclePictures values ('xtt6', 'r1m_1.png')
insert into dbo.vehiclePictures values ('xtt6', 'r1m_2.png')
insert into dbo.vehiclePictures values ('xtt6', 'r1m_3.png')
USE [master]
GO
ALTER DATABASE [WebMotor] SET  READ_WRITE 
GO
