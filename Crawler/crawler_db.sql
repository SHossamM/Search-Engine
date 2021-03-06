USE [master]
GO
/****** Object:  Database [SearchEngine1]    Script Date: 3/14/2018 10:50:24 PM ******/
CREATE DATABASE [SearchEngine1]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SearchEngine', FILENAME = N'C:\Program Files (x86)\Microsoft SQL Server\MSSQL12.SQLEXPRESS\MSSQL\DATA\SearchEngine.mdf' , SIZE = 12288KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'SearchEngine_log', FILENAME = N'C:\Program Files (x86)\Microsoft SQL Server\MSSQL12.SQLEXPRESS\MSSQL\DATA\SearchEngine_log.ldf' , SIZE = 2048KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [SearchEngine1] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SearchEngine1].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SearchEngine1] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SearchEngine1] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SearchEngine1] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SearchEngine1] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SearchEngine1] SET ARITHABORT OFF 
GO
ALTER DATABASE [SearchEngine1] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [SearchEngine1] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SearchEngine1] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SearchEngine1] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SearchEngine1] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SearchEngine1] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SearchEngine1] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SearchEngine1] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SearchEngine1] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SearchEngine1] SET  DISABLE_BROKER 
GO
ALTER DATABASE [SearchEngine1] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SearchEngine1] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SearchEngine1] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SearchEngine1] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SearchEngine1] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SearchEngine1] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SearchEngine1] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SearchEngine1] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [SearchEngine1] SET  MULTI_USER 
GO
ALTER DATABASE [SearchEngine1] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SearchEngine1] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SearchEngine1] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SearchEngine1] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [SearchEngine1] SET DELAYED_DURABILITY = DISABLED 
GO
USE [SearchEngine1]
GO
/****** Object:  Table [dbo].[ContainsUrl]    Script Date: 3/14/2018 10:50:24 PM ******/
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
/****** Object:  Table [dbo].[NotVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NotVisited](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [nvarchar](max) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[SeedSet]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SeedSet](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [varchar](900) NOT NULL,
	[visited] [int] NOT NULL,
	[parseable] [int] NOT NULL,
	[verified] [int] NOT NULL,
	[robotallowed] [int] NOT NULL,
 CONSTRAINT [PK_SeedSet] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Visited]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Visited](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [nvarchar](max) NOT NULL,
	[DocumentNo] [int] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Index [IX_SeedSet_URL]    Script Date: 3/14/2018 10:50:24 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [IX_SeedSet_URL] ON [dbo].[SeedSet]
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_visited]  DEFAULT ((0)) FOR [visited]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_parsable]  DEFAULT ((1)) FOR [parseable]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_verified]  DEFAULT ((1)) FOR [verified]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_robotallowed]  DEFAULT ((1)) FOR [robotallowed]
GO
ALTER TABLE [dbo].[ContainsUrl]  WITH CHECK ADD  CONSTRAINT [FK_Destination_ID] FOREIGN KEY([destinationId])
REFERENCES [dbo].[SeedSet] ([id])
GO
ALTER TABLE [dbo].[ContainsUrl] CHECK CONSTRAINT [FK_Destination_ID]
GO
ALTER TABLE [dbo].[ContainsUrl]  WITH CHECK ADD  CONSTRAINT [FK_Source_ID] FOREIGN KEY([sourceId])
REFERENCES [dbo].[SeedSet] ([id])
GO
ALTER TABLE [dbo].[ContainsUrl] CHECK CONSTRAINT [FK_Source_ID]
GO
/****** Object:  StoredProcedure [dbo].[CheckUrlExists]    Script Date: 3/14/2018 10:50:24 PM ******/
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
/****** Object:  StoredProcedure [dbo].[CheckUrlExistsN]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[CheckUrlExistsN](@u varchar(Max), @found int output)
	-- Add the parameters for the stored procedure here
	
AS
 Begin

    -- Insert statements for procedure here
		IF Exists
		(
		 select * from NotVisited where url=@u
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
/****** Object:  StoredProcedure [dbo].[CheckUrlExistsV]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[CheckUrlExistsV](@u varchar(Max), @found int output)
	-- Add the parameters for the stored procedure here
	
AS
 Begin

    -- Insert statements for procedure here
		IF Exists
		(
		 select * from Visited where url=@u
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
/****** Object:  StoredProcedure [dbo].[CountVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[CountVisited](@count int output) 
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	

    -- Insert statements for procedure here
	SELECT  @count=count(*) from Visited;

END

GO
/****** Object:  StoredProcedure [dbo].[DeleteNonVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[DeleteNonVisited](@u varchar(MAX))
	-- Add the parameters for the stored procedure here
	
AS

delete  from NotVisited where url=@u ;

GO
/****** Object:  StoredProcedure [dbo].[InsertLink]    Script Date: 3/14/2018 10:50:24 PM ******/
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
		Insert into SeedSet(url) values(@destUrl);
		
		insert into ContainsUrl
		select @srcId, dest.id
		from SeedSet as dest
		where dest.url=@destUrl;

		END
END



GO
/****** Object:  StoredProcedure [dbo].[InsertLinkN]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[InsertLinkN](@u varchar(MAX))
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	Insert into NotVisited(url) values(@u);
END


GO
/****** Object:  StoredProcedure [dbo].[InsertLinkV]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[InsertLinkV](@u varchar(MAX),@d int)
	-- Add the parameters for the stored procedure here
	
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	Insert into Visited(url,DocumentNo) values(@u,@d);
END


GO
/****** Object:  StoredProcedure [dbo].[MarkVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
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
	update SeedSet set visited=1 where id=@urlId;
END


GO
/****** Object:  StoredProcedure [dbo].[RetriveAll]    Script Date: 3/14/2018 10:50:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[RetriveAll]
	-- Add the parameters for the stored procedure here
	
AS

select * from NotVisited ;

GO
/****** Object:  StoredProcedure [dbo].[RetriveNonVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
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

select top 500 url from NotVisited ;

GO
/****** Object:  StoredProcedure [dbo].[RetriveVisited]    Script Date: 3/14/2018 10:50:24 PM ******/
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

select  * from Visited ;

GO
/****** Object:  StoredProcedure [dbo].[UnmarkParseable]    Script Date: 3/14/2018 10:50:24 PM ******/
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
/****** Object:  StoredProcedure [dbo].[UnmarkRobotallowed]    Script Date: 3/14/2018 10:50:24 PM ******/
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
/****** Object:  StoredProcedure [dbo].[UnmarkVerified]    Script Date: 3/14/2018 10:50:24 PM ******/
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
USE [master]
GO
ALTER DATABASE [SearchEngine1] SET  READ_WRITE 
GO
