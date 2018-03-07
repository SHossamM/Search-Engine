USE [master]
GO
/****** Object:  Database [SearchEngine]    Script Date: 3/7/2018 3:49:53 PM ******/
CREATE DATABASE [SearchEngine]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SearchEngine', FILENAME = N'C:\Program Files (x86)\Microsoft SQL Server\MSSQL12.SQLEXPRESS\MSSQL\DATA\SearchEngine.mdf' , SIZE = 5120KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'SearchEngine_log', FILENAME = N'C:\Program Files (x86)\Microsoft SQL Server\MSSQL12.SQLEXPRESS\MSSQL\DATA\SearchEngine_log.ldf' , SIZE = 2048KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [SearchEngine] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SearchEngine].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SearchEngine] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SearchEngine] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SearchEngine] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SearchEngine] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SearchEngine] SET ARITHABORT OFF 
GO
ALTER DATABASE [SearchEngine] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [SearchEngine] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SearchEngine] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SearchEngine] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SearchEngine] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SearchEngine] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SearchEngine] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SearchEngine] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SearchEngine] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SearchEngine] SET  DISABLE_BROKER 
GO
ALTER DATABASE [SearchEngine] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SearchEngine] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SearchEngine] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SearchEngine] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SearchEngine] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SearchEngine] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SearchEngine] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SearchEngine] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [SearchEngine] SET  MULTI_USER 
GO
ALTER DATABASE [SearchEngine] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SearchEngine] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SearchEngine] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SearchEngine] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [SearchEngine] SET DELAYED_DURABILITY = DISABLED 
GO
USE [SearchEngine]
GO
/****** Object:  Table [dbo].[SeedSet]    Script Date: 3/7/2018 3:49:53 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SeedSet](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [varchar](max) NULL,
	[visited] [int] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  StoredProcedure [dbo].[InsertLink]    Script Date: 3/7/2018 3:49:53 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[InsertLink](@u varchar(MAX))
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	Insert into SeedSet(url) values(@u);
END


GO
/****** Object:  StoredProcedure [dbo].[RetriveNonVisited]    Script Date: 3/7/2018 3:49:53 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetriveNonVisited]
	-- Add the parameters for the stored procedure here
	
AS

select url from SeedSet where visited=0

GO
USE [master]
GO
ALTER DATABASE [SearchEngine] SET  READ_WRITE 
GO
