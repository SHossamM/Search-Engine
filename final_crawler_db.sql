/*    ==Scripting Parameters==

    Source Server Version : SQL Server 2014 (12.0.2569)
    Source Database Engine Edition : Microsoft SQL Server Enterprise Edition
    Source Database Engine Type : Standalone SQL Server

    Target Server Version : SQL Server 2017
    Target Database Engine Edition : Microsoft SQL Server Standard Edition
    Target Database Engine Type : Standalone SQL Server
*/
USE [master]
GO
/****** Object:  Database [SearchEngine]    Script Date: 5/2/2018 4:35:44 PM ******/
CREATE DATABASE [SearchEngine]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SearchEngine', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\SearchEngine.mdf' , SIZE = 65728KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'SearchEngine_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\SearchEngine_log.ldf' , SIZE = 94528KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
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
EXEC sys.sp_db_vardecimal_storage_format N'SearchEngine', N'ON'
GO
USE [SearchEngine]
GO
/****** Object:  User [SearchEngineUser]    Script Date: 5/2/2018 4:35:45 PM ******/
CREATE USER [SearchEngineUser] FOR LOGIN [SearchEngine] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_accessadmin] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_securityadmin] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_ddladmin] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_backupoperator] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_datareader] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_denydatareader] ADD MEMBER [SearchEngineUser]
GO
ALTER ROLE [db_denydatawriter] ADD MEMBER [SearchEngineUser]
GO
/****** Object:  Table [dbo].[ContainsUrl]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ContainsUrl](
	[sourceId] [int] NOT NULL,
	[destinationId] [int] NOT NULL,
 CONSTRAINT [PK_ContainsUrl] PRIMARY KEY CLUSTERED 
(
	[sourceId] ASC,
	[destinationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SeedSet]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SeedSet](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [varchar](900) NOT NULL,
	[visited] [datetime] NOT NULL,
	[parseable] [int] NOT NULL,
	[verified] [int] NOT NULL,
	[robotallowed] [int] NOT NULL,
	[HashContent] [int] NULL,
	[ToBeIndexed] [int] NOT NULL,
	[downloaded] [int] NOT NULL,
	[Rank] [decimal](18, 10) NOT NULL,
	[News] [int] NOT NULL,
 CONSTRAINT [PK_SeedSet] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [url_index]    Script Date: 5/2/2018 4:35:46 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [url_index] ON [dbo].[SeedSet]
(
	[url] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_visited]  DEFAULT (((1980)-(1))-(1)) FOR [visited]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_parsable]  DEFAULT ((1)) FOR [parseable]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_verified]  DEFAULT ((1)) FOR [verified]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_robotallowed]  DEFAULT ((1)) FOR [robotallowed]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_ToBeIndexed]  DEFAULT ((0)) FOR [ToBeIndexed]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_downloaded]  DEFAULT ((0)) FOR [downloaded]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_Rank]  DEFAULT ((0)) FOR [Rank]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_News]  DEFAULT ((0)) FOR [News]
GO
/****** Object:  StoredProcedure [dbo].[CheckDuplicate]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[CheckDuplicate](@hashed int, @found int output)
	-- Add the parameters for the stored procedure here
	
AS
 Begin

    -- Insert statements for procedure here
		IF Exists
		(
		 select * from SeedSet where HashContent=@hashed
		)
		BEGIN
		 set @found=1
		END
		ELSE 
		  BEGIN
			set @found= 0
		END
		
end



GO
/****** Object:  StoredProcedure [dbo].[CheckUrlExists]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[CheckUrlExists](@u varchar(Max), @found int output)
	-- Add the parameters for the stored procedure here
	
AS
 Begin

    -- Insert statements for procedure here
		IF Exists
		(
		 select * from SeedSet where url=@u
		)
		BEGIN
		 set @found=1
		END
		ELSE 
		  BEGIN
			set @found= 0
		END
		
end



GO
/****** Object:  StoredProcedure [dbo].[ClearDownloaded]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[ClearDownloaded]
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;


	update SeedSet set downloaded=0 where downloaded=1;
END
GO
/****** Object:  StoredProcedure [dbo].[CountVisited]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[CountVisited](@count int output) 
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	

    -- Insert statements for procedure here
	--DATEADD(hour,-1,SYSDATETIMEOFFSET())
	SELECT  @count=count(*)  from SeedSet where downloaded=1;

END



GO
/****** Object:  StoredProcedure [dbo].[InsertHash]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[InsertHash](@urlId int,@hash int)
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here

	--if hash content null insert new hash 

	

    -- Insert statements for procedure here
    IF  Exists
		(
		 select 1 from SeedSet where id=@urlId and (HashContent is Null or HashContent!=@hash)
		)
		BEGIN

		Delete from ContainsUrl where sourceId=@urlId;

        update SeedSet set  HashContent=@hash, ToBeIndexed=1, downloaded=1 where id=@urlId;

        END

END

	















GO
/****** Object:  StoredProcedure [dbo].[InsertLink]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[InsertLink](@srcId int, @destUrl varchar(900))
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
    IF NOT Exists
		(
		 select 1 from SeedSet where url=@destUrl
		)
		BEGIN
		Insert into SeedSet(url,visited) values(@destUrl,'1905-06-02 00:00:00.000');
		END
		

	
	Declare @destid int;
	select @destid = id from SeedSet where url=@destUrl;

	IF NOT Exists
		(
		 select 1 from ContainsUrl where sourceId=@srcId and destinationId=@destid
		)
		BEGIN
		
		insert into ContainsUrl
		select @srcId, @destid;
		
		END

END




GO
/****** Object:  StoredProcedure [dbo].[MarkIndexed]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[MarkIndexed](@urlid int)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set ToBeIndexed=0 where id=@urlid;
END
GO
/****** Object:  StoredProcedure [dbo].[MarkNews]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[MarkNews](@urlId int) 
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set News=1 where id=@urlId;
END
GO
/****** Object:  StoredProcedure [dbo].[MarkVisited]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[MarkVisited](@urlId int)
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here

	--if hash content null insert new hash 

	

    -- Insert statements for procedure here
    IF  Exists
		(
		 select 1 from SeedSet where id=@urlId --and HashedContent IS NULL
		)
		BEGIN

         Update SeedSet set  visited=GETDATE() where id=@urlId 

        END


	--IF Exists
	--(
	--select 1 from SeedSet where id=@urlId and HashedContent != HASHBYTES('SHA',@content)

	--)
	--BEGIN

 --        Update SeedSet set  HashedContent=HASHBYTES('SHA',@content) ,ToBeIndexed=1 where id=@urlId 

 --       END

END

	















GO
/****** Object:  StoredProcedure [dbo].[RetrieveDownloaded]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetrieveDownloaded]
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SELECT id from SeedSet where HashContent is not NULL;
END
GO
/****** Object:  StoredProcedure [dbo].[RetrieveToBeIndexed]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetrieveToBeIndexed] 
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select id,url from SeedSet where ToBeIndexed=1;
END
GO
/****** Object:  StoredProcedure [dbo].[RetrieveToBeRanked]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetrieveToBeRanked] 
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select sourceId, destinationId
	from ContainsUrl
	inner join SeedSet as S1 on S1.id = sourceId
	inner join SeedSet as S2 on S2.id = destinationId
	where S1.HashContent is not NULL
	and S1.ToBeIndexed=0
	and S2.HashContent is not NULL
	and S2.ToBeIndexed=0;
END
GO
/****** Object:  StoredProcedure [dbo].[RetriveAll]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[RetriveAll]
	-- Add the parameters for the stored procedure here
	
AS

select * from SeedSet ;



GO
/****** Object:  StoredProcedure [dbo].[RetriveNonVisited]    Script Date: 5/2/2018 4:35:46 PM ******/
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

select top 100 * from SeedSet where visited <= DATEADD(hour,-1,SYSDATETIMEOFFSET());



GO
/****** Object:  StoredProcedure [dbo].[RetriveVisited]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetriveVisited]
	-- Add the parameters for the stored procedure here
	
AS

select  url from SeedSet where visited>GETDATE()-7 and parseable=1 and verified=1 and robotallowed=1 and downloaded=1;



GO
/****** Object:  StoredProcedure [dbo].[UnmarkParseable]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[UnmarkParseable](@urlId int)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set parseable=0 where id=@urlId;
END


GO
/****** Object:  StoredProcedure [dbo].[UnmarkRobotallowed]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[UnmarkRobotallowed](@urlId int)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set robotallowed=0 where id=@urlId;
END


GO
/****** Object:  StoredProcedure [dbo].[UnmarkVerified]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[UnmarkVerified](@urlId int)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set verified=0 where id=@urlId;
END


GO
/****** Object:  StoredProcedure [dbo].[UpdatePageRank]    Script Date: 5/2/2018 4:35:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[UpdatePageRank](@urlid int, @pagerank decimal(18,10))
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	update SeedSet set Rank=@pagerank where id=@urlid;
END
GO
USE [master]
GO
ALTER DATABASE [SearchEngine] SET  READ_WRITE 
GO
